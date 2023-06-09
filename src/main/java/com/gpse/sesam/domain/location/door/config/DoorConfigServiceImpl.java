package com.gpse.sesam.domain.location.door.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpse.sesam.configuration.DoorApiConfig;
import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.domain.credential.credentials.CredentialService;
import com.gpse.sesam.domain.credential.issuing.FormEntry;
import com.gpse.sesam.domain.credential.issuing.FormEntryType;
import com.gpse.sesam.web.cmd.AttributeFilterCmd;
import com.gpse.sesam.web.cmd.ConfigPartsViewCmd;
import com.gpse.sesam.web.cmd.DoorConfigViewCmd;
import com.gpse.sesam.web.exception.InvalidDoorConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class DoorConfigServiceImpl implements DoorConfigService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DoorConfigServiceImpl.class);

	private final DoorApiConfig appConfig;
	private final ProofConfigRepository proofConfigRepository;
	private final CredentialService credentialService;

	@Autowired
	public DoorConfigServiceImpl(final DoorApiConfig appConfig, final ProofConfigRepository proofConfigRepository,
								 final CredentialService credentialService) {
		this.appConfig = appConfig;
		this.proofConfigRepository = proofConfigRepository;
		this.credentialService = credentialService;
		proofConfigRepository.save(createProofConfig());
	}

	@Override
	public void getDoorConfigurations() {
		final HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(appConfig.getUsername(), appConfig.getPassword());

		final HttpEntity<String> entity = new HttpEntity<>(headers);

		final RestTemplate restTemplate = new RestTemplate();

		final ResponseEntity<String> response = restTemplate.exchange(appConfig.getUrl() + "/api/proof/config",
				HttpMethod.GET, entity, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			LOGGER.info("Anmeldung erfolgreich!");
			final String responseBody = response.getBody();
			LOGGER.info("Antwort: " + responseBody);
		} else {
			LOGGER.error("Anmeldung fehlgeschlagen. Statuscode: " + response.getStatusCode());
		}
	}

	@Override
	public DoorConfigViewCmd getDoorConfig(final String doorApiId) {
		final HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(appConfig.getUsername(), appConfig.getPassword());

		final HttpEntity<String> entity = new HttpEntity<>(headers);

		final RestTemplate restTemplate = new RestTemplate();

		final ResponseEntity<String> response =
				restTemplate.exchange(appConfig.getUrl() + "/api/proof/config/" + doorApiId,
						HttpMethod.GET, entity, String.class);

		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			final ProofConfig proofConfig = objectMapper.readValue(response.getBody(), ProofConfig.class);
			final DoorConfigViewCmd doorConfigCmd = new DoorConfigViewCmd();
			doorConfigCmd.setDescription(proofConfig.getDescription());

			for (final ProofAttributeInfo attributeInfoEntry : proofConfig.getRequestedAttributes().values()) {

				final ConfigPartsViewCmd configPartsViewCmd = new ConfigPartsViewCmd();

				final List<Credential> credentials = attributeInfoEntry
						.getRestrictions()
						.stream()
						.filter(predicate -> predicate.getCredentialDefinitionId() != null)
						.flatMap(predicate ->
								credentialService
										.getCredentialByCredentialIssuerId(predicate.getCredentialDefinitionId())
										.stream()
										.distinct())
						.toList();

				configPartsViewCmd.setCredentials(credentials);

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
										simpleDateFormat.format(parser.parse(attributeFilterCmd.getValue()))
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


				configPartsViewCmd.setAttributeFilter(attributeFilterCmds);
				doorConfigCmd.addConfigPart(configPartsViewCmd);

			}

			for (final ProofPredicateInfo proofPredicateInfo : proofConfig.getRequestedPredicates().values()) {

				final ConfigPartsViewCmd configPartsViewCmd = new ConfigPartsViewCmd();

				final List<Credential> credentials = proofPredicateInfo
						.getRestrictions()
						.stream()
						.filter(predicate -> predicate.getCredentialDefinitionId() != null)
						.flatMap(predicate ->
								credentialService
										.getCredentialByCredentialIssuerId(predicate.getCredentialDefinitionId())
										.stream()
										.distinct())
						.toList();

				configPartsViewCmd.setCredentials(credentials);

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
									simpleDateFormat.format(parser.parse(attributeFilterCmd.getValue()))
							);
						}
					} else {
						attributeFilterCmd.setValue(proofPredicateInfo.getPredicateValue());
					}
					attributeFilterCmd.setAttribute(formEntry.get());
					attributeFilterCmd.setPredicateType(Predicate.fromString(proofPredicateInfo.getPredicateType()));
				}

				configPartsViewCmd.setAttributeFilter(Collections.singletonList(attributeFilterCmd));
				doorConfigCmd.addConfigPart(configPartsViewCmd);

			}
			return doorConfigCmd;
		} catch (final JsonProcessingException | ParseException e) {
			throw new InvalidDoorConfiguration("could not read door configuration", e);
		}
	}

	@Override
	public void sendProofConfig(final String doorId, final ProofConfig proofConfig) {
		final String url = appConfig.getUrl() + "/api/proof/config/" + doorId;

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(appConfig.getUsername(), appConfig.getPassword());

		try {
			final ObjectMapper objectMapper = new ObjectMapper();
			final String requestBody = objectMapper.writeValueAsString(proofConfig);
			final HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

			LOGGER.info(requestBody);

			final RestTemplate restTemplate = new RestTemplate();
			final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

			if (!response.getStatusCode().is2xxSuccessful()) {
				LOGGER.error("POST Request fehlgeschlagen. Statuscode: {} ", response.getStatusCode());
				throw new InvalidDoorConfiguration("Could not configure door. Reason: " + response.getBody());
			}
		} catch (final JsonProcessingException e) {
			LOGGER.error("Fehler beim Erstellen des Request-Bodies.", e);
		}
	}

	private ProofConfig createProofConfig() {
		final ProofConfig proofConfig = new ProofConfig();
		proofConfig.setDescription("Bitte präsentieren Sie ein T-Mitglieds-Credential mit dem Vornamen David.");

		final Map<String, ProofAttributeInfo> requestedAttributes = new HashMap<>();
		final ProofAttributeInfo attributeInfo = new ProofAttributeInfo();
		attributeInfo.setName("first_name");
		final List<AttributeFilter> restrictions = new ArrayList<>();
		restrictions.add(new AttributeFilter());
		restrictions.get(0).setAttributeValue(new AttributeValue("first_name", "David"));
		restrictions.add(new AttributeFilter());
		restrictions.get(1).setCredentialDefinitionId("$T-MEMBER");
		attributeInfo.setRestrictions(restrictions);
		requestedAttributes.put("first_name_test", attributeInfo);
		proofConfig.setRequestedAttributes(requestedAttributes);

		final Map<String, ProofPredicateInfo> requestedPredicates = new HashMap<>();
		final ProofPredicateInfo predicateInfo = new ProofPredicateInfo();
		predicateInfo.setName("expiration_date");
		predicateInfo.setPredicateType(">");
		predicateInfo.setPredicateValue("$TODAY-YYYYMMDD");
		final List<AttributeFilter> predicateRestrictions = new ArrayList<>();
		predicateRestrictions.add(new AttributeFilter());
		predicateRestrictions.get(0).setCredentialDefinitionId("$T-MEMBER");
		predicateInfo.setRestrictions(predicateRestrictions);
		requestedPredicates.put("expiration_date", predicateInfo);
		proofConfig.setRequestedPredicates(requestedPredicates);

		return proofConfig;
	}
}
