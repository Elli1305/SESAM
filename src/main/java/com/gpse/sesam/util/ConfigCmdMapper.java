package com.gpse.sesam.util;

import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.domain.credential.credentials.internal.CredentialService;
import com.gpse.sesam.domain.credential.issue.issuing.FormEntry;
import com.gpse.sesam.domain.credential.issue.issuing.FormEntryType;
import com.gpse.sesam.domain.location.door.config.AttributeFilter;
import com.gpse.sesam.domain.location.door.config.AttributeValue;
import com.gpse.sesam.domain.location.door.config.Predicate;
import com.gpse.sesam.domain.location.door.config.ProofAttributeInfo;
import com.gpse.sesam.domain.location.door.config.ProofConfig;
import com.gpse.sesam.domain.location.door.config.ProofPredicateInfo;
import com.gpse.sesam.web.cmd.AttributeFilterCmd;
import com.gpse.sesam.web.cmd.ConfigPartsCmd;
import com.gpse.sesam.web.cmd.DoorConfigCmd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class ConfigCmdMapper {

	private CredentialService credentialService;

	public ConfigCmdMapper(CredentialService credentialService) {
		this.credentialService = credentialService;
	}

	public static ProofConfig fromCmd(final DoorConfigCmd doorConfigCmd) {
		final ProofConfig proofConfig = new ProofConfig();

		proofConfig.setDescription(doorConfigCmd.getDescription());

		int index = 0;
		for (final ConfigPartsCmd part : doorConfigCmd.getConfigParts()) {
			final ProofAttributeInfo attributeInfo = new ProofAttributeInfo();
			final List<AttributeFilter> attributeFilters = new ArrayList<>();
			for (final AttributeFilterCmd filter : part.getAttributeFilter()) {
				if (Predicate.EQUAL.equals(filter.getPredicateType())) {
					attributeInfo.setName(filter.getAttribute().getAttributeName());

					final AttributeFilter attributeFilter = new AttributeFilter();


					final AttributeValue attributeValue = new AttributeValue();
					attributeValue.setName(filter.getAttribute().getAttributeName());
					if (filter.getAttribute().getType().equals(FormEntryType.DATE)) {
						if (filter.isCurrentDate()) {
							attributeValue.setValue("$TODAY-YYYYMMDD");
						} else {
							attributeValue.setValue(filter.getValue().replace("-", ""));
						}
					} else {
						attributeValue.setValue(filter.getValue());
					}

					attributeFilter.setAttributeValue(attributeValue);
					attributeFilters.add(attributeFilter);

					attributeInfo.setRestrictions(attributeFilters);
				} else {
					final ProofPredicateInfo proofPredicateInfo = new ProofPredicateInfo();
					proofPredicateInfo.setPredicateType(filter.getPredicateType().getValue());
					proofPredicateInfo.setName(filter.getAttribute().getAttributeName());

					if (filter.getAttribute().getType().equals(FormEntryType.DATE)) {
						if (filter.isCurrentDate()) {
							proofPredicateInfo.setPredicateValue("$TODAY-YYYYMMDD");
						} else {
							proofPredicateInfo.setPredicateValue(filter.getValue().replace("-", ""));
						}
					} else {
						proofPredicateInfo.setPredicateValue(filter.getValue());
					}

					final List<AttributeFilter> restrictions = new ArrayList<>();
					for (final Credential credential : part.getCredentials()) {
						final AttributeFilter attributeFilter = new AttributeFilter();
						attributeFilter.setCredentialDefinitionId(credential.getCredentialDefinitionId());

						restrictions.add(attributeFilter);
					}
					proofPredicateInfo.setRestrictions(restrictions);

					proofConfig.addRequestedPredicate(filter.getAttribute()
							.getAttributeName() + (index++), proofPredicateInfo);
				}
			}
			for (final Credential credential : part.getCredentials()) {
				if (!attributeFilters.isEmpty()) {
					final AttributeFilter attributeFilter = new AttributeFilter();
					attributeFilter.setCredentialDefinitionId(credential.getCredentialDefinitionId());
					attributeFilters.add(attributeFilter);

					attributeInfo.setRestrictions(attributeFilters);
				}
			}
			if (!attributeFilters.isEmpty()) {
				proofConfig.addRequestedAttributes(attributeInfo.getName() + (index++), attributeInfo);
			}
		}
		return proofConfig;
	}

	public DoorConfigCmd toCmd(ProofConfig proofConfig) throws ParseException {
		final DoorConfigCmd doorConfigCmd = new DoorConfigCmd();
		doorConfigCmd.setDescription(proofConfig.getDescription());

		for (final ProofAttributeInfo attributeInfoEntry : proofConfig.getRequestedAttributes().values()) {

			final ConfigPartsCmd configPartsCmd = new ConfigPartsCmd();

			final List<Credential> credentials = attributeInfoEntry
					.getRestrictions()
					.stream()
					.filter(predicate -> predicate.getCredentialDefinitionId() != null)
					.flatMap(predicate ->
							credentialService
									.getCredentialByCredentialDefinitionId(predicate.getCredentialDefinitionId())
									.stream()
									.distinct())
					.toList();

			configPartsCmd.setCredentials(credentials);

			final List<AttributeValue> attributeValues = attributeInfoEntry
					.getRestrictions()
					.stream()
					.map(AttributeFilter::getAttributeValue)
					.filter(Objects::nonNull)
					.toList();


			final List<FormEntry> formEntries = credentials.stream()
					.flatMap(credential -> credential.getForm().stream())
					.toList();

			final List<AttributeFilterCmd> attributeFilterCmds = new ArrayList<>();

			for (final AttributeValue attributeValue : attributeValues) {
				final AttributeFilterCmd attributeFilterCmd = new AttributeFilterCmd();

				final Optional<FormEntry> formEntry = formEntries.stream()
						.filter(fe -> fe.getAttributeName().equals(attributeValue.getName())).findFirst();

				if (formEntry.isPresent()) {
					if (formEntry.get().getType().equals(FormEntryType.DATE)) {
						final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						if (attributeValue.getValue().equals("$TODAY-YYYYMMDD")) {
							attributeFilterCmd.setCurrentDate(true);
							attributeFilterCmd.setValue(simpleDateFormat.format(new Date()));
						} else {
							final SimpleDateFormat parser = new SimpleDateFormat("yyyyMMdd");
							attributeFilterCmd.setValue(
									simpleDateFormat.format(parser.parse(attributeValue.getValue()))
							);
						}
					} else {
						attributeFilterCmd.setValue(attributeValue.getValue());

					}
					attributeFilterCmd.setAttribute(formEntry.get());
					attributeFilterCmd.setPredicateType(Predicate.EQUAL);
					attributeFilterCmds.add(attributeFilterCmd);
				}

			}


			configPartsCmd.setAttributeFilter(attributeFilterCmds);
			doorConfigCmd.addConfigPart(configPartsCmd);

		}

		for (final ProofPredicateInfo proofPredicateInfo : proofConfig.getRequestedPredicates().values()) {

			final ConfigPartsCmd configPartsCmd = new ConfigPartsCmd();

			final List<Credential> credentials = proofPredicateInfo
					.getRestrictions()
					.stream()
					.filter(predicate -> predicate.getCredentialDefinitionId() != null)
					.flatMap(predicate ->
							credentialService
									.getCredentialByCredentialDefinitionId(predicate.getCredentialDefinitionId())
									.stream()
									.distinct())
					.toList();

			configPartsCmd.setCredentials(credentials);

			final List<FormEntry> formEntries = credentials.stream()
					.flatMap(credential -> credential.getForm().stream())
					.toList();


			final Optional<FormEntry> formEntry = formEntries.stream()
					.filter(fe -> fe.getAttributeName().equals(proofPredicateInfo.getName()))
					.findFirst();

			final AttributeFilterCmd attributeFilterCmd = new AttributeFilterCmd();

			if (formEntry.isPresent()) {
				if (formEntry.get().getType().equals(FormEntryType.DATE)) {
					final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					if (proofPredicateInfo.getPredicateValue().equals("$TODAY-YYYYMMDD")) {
						attributeFilterCmd.setCurrentDate(true);
						attributeFilterCmd.setValue(simpleDateFormat.format(new Date()));
					} else {
						final SimpleDateFormat parser = new SimpleDateFormat("yyyyMMdd");
						attributeFilterCmd.setValue(
								simpleDateFormat.format(parser.parse(proofPredicateInfo.getPredicateValue()))
						);
					}
				} else {
					attributeFilterCmd.setValue(proofPredicateInfo.getPredicateValue());
				}
				attributeFilterCmd.setAttribute(formEntry.get());
				attributeFilterCmd.setPredicateType(Predicate.fromString(proofPredicateInfo.getPredicateType()));
			}

			configPartsCmd.setAttributeFilter(Collections.singletonList(attributeFilterCmd));
			doorConfigCmd.addConfigPart(configPartsCmd);

		}
		return doorConfigCmd;
	}
}
