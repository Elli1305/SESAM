package com.gpse.sesam.domain.location.door.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpse.sesam.configuration.DoorApiConfig;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DoorConfigServiceImpl implements DoorConfigService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DoorConfigServiceImpl.class);

	private final DoorApiConfig appConfig;
	private final ProofConfigRepository proofConfigRepository;

	@Autowired
	public DoorConfigServiceImpl(final DoorApiConfig appConfig, final ProofConfigRepository proofConfigRepository) {
		this.appConfig = appConfig;
		this.proofConfigRepository = proofConfigRepository;
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
	public void sendProofConfig(String doorId, ProofConfig proofConfig) {
		//proofConfig.setCreatedAt(new Date());

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
		} catch (JsonProcessingException e) {
			LOGGER.error("Fehler beim Erstellen des Request-Bodies.", e);
		}
	}


	private ProofConfig createProofConfig() {
		final ProofConfig proofConfig = new ProofConfig();
		proofConfig.setDescription("Bitte präsentieren Sie ein T-Mitglieds-Credential mit dem Vornamen David.");
		//proofConfig.setCreatedAt(new Date());

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
