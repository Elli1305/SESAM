package com.gpse.sesam.util;

import com.gpse.sesam.domain.credential.issuing.FormEntryType;
import com.gpse.sesam.domain.location.door.config.AttributeFilter;
import com.gpse.sesam.domain.location.door.config.AttributeValue;
import com.gpse.sesam.domain.location.door.config.Predicate;
import com.gpse.sesam.domain.location.door.config.ProofAttributeInfo;
import com.gpse.sesam.domain.location.door.config.ProofConfig;
import com.gpse.sesam.domain.location.door.config.ProofPredicateInfo;
import com.gpse.sesam.web.cmd.AttributeFilterCmd;
import com.gpse.sesam.web.cmd.ConfigPartsCmd;
import com.gpse.sesam.web.cmd.DoorConfigCmd;

import java.util.ArrayList;
import java.util.List;

public final class ConfigCmdMapper {


	private ConfigCmdMapper() {
	}

	public static ProofConfig fromCmd(final DoorConfigCmd doorConfigCmd) {
		final ProofConfig proofConfig = new ProofConfig();

		proofConfig.setDescription(doorConfigCmd.getDescription());

		int index = 0;
		for (final ConfigPartsCmd part : doorConfigCmd.getConfigParts()) {
			final ProofAttributeInfo attributeInfo = new ProofAttributeInfo();
			final List<AttributeFilter> attributeFilters = new ArrayList<>();
			for (final AttributeFilterCmd filter : part.getAttributeFilter()) {
				if (filter.getPredicateType().equals(Predicate.EQUAL)) {
					attributeInfo.setName(filter.getAttribute().getAttributeName());

					final AttributeFilter attributeFilter = new AttributeFilter();
					attributeFilter.setAttributeValue(new AttributeValue(filter.getAttribute()
							.getAttributeName(), filter.getValue()));
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
					for (final String credentialDefinitionId : part.getCredentials()) {
						final AttributeFilter attributeFilter = new AttributeFilter();
						attributeFilter.setCredentialDefinitionId(credentialDefinitionId);

						restrictions.add(attributeFilter);
					}
					proofPredicateInfo.setRestrictions(restrictions);

					proofConfig.addRequestedPredicate(filter.getAttribute()
							.getAttributeName() + (index++), proofPredicateInfo);
				}
			}
			for (final String credentialDefinitionId : part.getCredentials()) {
				if (!attributeFilters.isEmpty()) {
					final AttributeFilter attributeFilter = new AttributeFilter();
					attributeFilter.setCredentialDefinitionId(credentialDefinitionId);
					attributeFilters.add(attributeFilter);

					attributeInfo.setRestrictions(attributeFilters);
				}
			}
			if (!attributeFilters.isEmpty()) {
				proofConfig.addRequestedAttributes(attributeInfo.getName() + (index++), attributeInfo);
			}
		}

		proofConfig.setEndTime(configCmd.getEndTime());
		proofConfig.setStartTime(configCmd.getStartTime());

		return proofConfig;
	}

}
