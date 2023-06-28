package com.gpse.sesam.domain.credential.credentials.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredential;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredentialRepository;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredentialService;
import com.gpse.sesam.domain.credential.issuing.ChecklistEntry;
import com.gpse.sesam.domain.credential.issuing.FormEntry;
import com.gpse.sesam.domain.credential.issuing.FormEntryType;
import com.gpse.sesam.domain.credential.issuing.IssueCredential;
import com.gpse.sesam.domain.credential.issuing.IssueCredentialAttribute;
import com.gpse.sesam.domain.credential.issuing.IssueCredentialRequest;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.domain.location.door.config.AttributeFilter;
import com.gpse.sesam.domain.user.issuer.Issuer;
import com.gpse.sesam.domain.user.issuer.IssuerRepository;
import com.gpse.sesam.web.cmd.CreateCredentialCmd;
import com.gpse.sesam.web.cmd.CredentialCmd;
import com.gpse.sesam.web.cmd.IssueCredentialAttributeCmd;
import com.gpse.sesam.web.cmd.UpdateAttributeCmd;
import com.gpse.sesam.web.cmd.UpdateConditionCmd;
import com.gpse.sesam.web.cmd.UpdateCredentialCmd;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CredentialServiceImpl implements CredentialService {

	private final WebClient client;

	private final ObjectMapper mapper;

	private final IssuerRepository issuerRepository;
	private final CredentialRepository credentialRepository;

	private final LocationService locationService;

	private final ExternalCredentialRepository externalCredentialRepository;
	private final ExternalCredentialService externalCredentialService;

	@Autowired
	public CredentialServiceImpl(final WebClient client, final ObjectMapper mapper,
								 final CredentialRepository credentialRepository,
								 final IssuerRepository issuerRepository,
								 final ExternalCredentialRepository externalCredentialRepository,
								 final LocationService locationService,
								 final ExternalCredentialService externalCredentialService) {
		this.client = client;
		this.mapper = mapper;
		this.issuerRepository = issuerRepository;
		this.credentialRepository = credentialRepository;
		this.locationService = locationService;
		this.externalCredentialRepository = externalCredentialRepository;
		this.externalCredentialService = externalCredentialService;
	}

	@Override
	public List<InternalCredential> getCredentials() {
		final List<InternalCredential> credentials = new ArrayList<>();
		credentialRepository.findAll().forEach(credentials::add);
		return credentials;
	}

	@Override
	public List<Credential> getAllCredentials() {
		final List<Credential> credentials = new ArrayList<>();
		credentialRepository.findAll().forEach(credentials::add);
		credentials.addAll(externalCredentialService.getExternalCredentials());
		return credentials;
	}

	@Override
	public List<InternalCredential> getCredentialsByIssuerId(final Long id) {
		final Issuer issuer = issuerRepository.findById(String.valueOf(id)).orElseThrow();
		return issuer.getCredentials();
	}

	@Override
	public List<ExternalCredential> getExternalCredentials() {
		final List<ExternalCredential> credentials = new ArrayList<>();
		externalCredentialRepository.findAll().forEach(credentials::add);
		return credentials;
	}

	@Override
	public List<Credential> getCredentialByCredentialDefinitionId(final String id) {
		List<Credential> credential = new ArrayList<>(credentialRepository.findAllByCredentialDefinitionId(id));
		if (credential.isEmpty()) {
			credential.addAll(externalCredentialService.getExternalCredentialByCredentialDefinitionId(id));
		}
		return credential;
	}


	@Override
	public Optional<InternalCredential> getCredential(final Long id) {
		return credentialRepository.findById(id);
	}

	private String sendCredentialIssueRequest(@Valid final IssueCredentialRequest issueCredentialRequest)
			throws JsonProcessingException {
		return client.post().uri("credential/issue").contentType(MediaType.TEXT_PLAIN)
				.accept(MediaType.APPLICATION_JSON).bodyValue(mapper.writeValueAsString(issueCredentialRequest))
				.retrieve().bodyToMono(String.class).timeout(Duration.ofMillis(5000)).block();
	}

	@Override
	public String issueCredential(final Long id, final List<IssueCredentialAttributeCmd> attributeCmds)
			throws JsonProcessingException {
		final InternalCredential credential = credentialRepository.findById(id).orElseThrow();

		final Map<Long, IssueCredentialAttributeCmd> attributeCmdMap = attributeCmds.stream()
				.collect(Collectors.toMap(IssueCredentialAttributeCmd::id, Function.identity()));

		final List<IssueCredentialAttribute> attributes = credential.getForm().stream().map(entry -> {
			final IssueCredentialAttributeCmd correspondingAttributeCmd = attributeCmdMap.get(entry.getId());
			if (correspondingAttributeCmd == null) {
				return null;
			}
			boolean isValid = entry.getValidationRules().stream().allMatch(rule -> rule.validate(correspondingAttributeCmd.value(), entry.getType()));
			if (!isValid) {
				throw new IllegalArgumentException("Input " + correspondingAttributeCmd.value() + " for attribute " + entry.getAttributeName() + " is not valid");
			}

			return new IssueCredentialAttribute(entry.getAttributeName(), entry.getType() == FormEntryType.DATE
					? correspondingAttributeCmd.value().replace("-", "")
					: correspondingAttributeCmd.value(), entry.getType());
		}).filter(Objects::nonNull).collect(Collectors.toList());

		if (credential.getForm().size() != attributes.size()) {
			return null;
		}

		return sendCredentialIssueRequest(new IssueCredentialRequest(credential.getAgent(),
				new IssueCredential(credential.getCredentialDefinitionId(), attributes)));
	}

	@Override
	public void saveAll(final Iterable<InternalCredential> credentials) {
		credentialRepository.saveAll(credentials);
	}

	@Override
	public List<CredentialCmd> getCredentialByLocation(Long id) {

		final Location location = locationService.getLocation(id)
				.orElseThrow(() -> new IllegalArgumentException("Location with id " + id + " does not exist"));

		final Iterable<InternalCredential> credentials = getCredentialFromAttachedProofConfig(location);

		final List<CredentialCmd> cmds = new ArrayList<>();

		for (final InternalCredential credential : credentials) {

			final String credentialName = credential.getName();
			final List<String> externalCredentials = new ArrayList<>();
			String categoryName = "";
			if (credential.getCategory() != null) {
				categoryName = credential.getCategory().getName();
				for (final ExternalCredential externalCredential : credential.getCategory().getExternalCredentials()) {
					final String external = externalCredential.getName();
					externalCredentials.add(external);
				}
			}
			final List<String> issuers = new ArrayList<>();
			final List<String> rooms = new ArrayList<>();
			for (final Issuer issuer : credential.getIssuer()) {
				final String issuerName = issuer.getFirstName() + " " + issuer.getLastName();
				final String room = issuer.getRoom().getName();
				issuers.add(issuerName);
				rooms.add(room);
			}
			cmds.add(new CredentialCmd(categoryName, credentialName, externalCredentials, issuers, rooms));
		}

		return cmds;
	}

	private Iterable<InternalCredential> getCredentialFromAttachedProofConfig(Location location) {
		return location
				.getBuildings().stream()
				.flatMap(building -> building.getFloors().stream())
				.flatMap(floor -> floor.getRooms().stream())
				.flatMap(room -> room.getDoors().stream())
				.flatMap(door -> Stream.concat(door.getProofConfigIn().stream(), door.getProofConfigOut()
						.stream()))
				.flatMap(proofConfig -> {
					final Stream<String> attributeFilterStream = proofConfig.getRequestedPredicates().values()
							.stream()
							.flatMap(proofPredicateInfo -> proofPredicateInfo.getRestrictions().stream())
							.map(AttributeFilter::getCredentialDefinitionId);
					final Stream<String> attributeFilterStream1 = proofConfig.getRequestedAttributes().values()
							.stream()
							.flatMap(proofAttributeInfo -> proofAttributeInfo.getRestrictions().stream())
							.map(AttributeFilter::getCredentialDefinitionId);
					return Stream.concat(attributeFilterStream, attributeFilterStream1);
				})
				.filter(Objects::nonNull)
				.flatMap(definitionId -> credentialRepository.findAllByCredentialDefinitionId(definitionId)
						.stream())
				.collect(Collectors.toSet());
	}

	@Override
	public void create(final CreateCredentialCmd createCredentialCmd) {
		final InternalCredential credential = new InternalCredential(
				createCredentialCmd.getName(),
				createCredentialCmd.getCredentialDefinitionId(),
				createCredentialCmd.getAgent(),
				createCredentialCmd.getAttributes().stream()
						.map(createAttributeCmd ->
								new FormEntry(
										createAttributeCmd.getName(),
										createAttributeCmd.getType(),
										createAttributeCmd.getAttributeName(),
										createAttributeCmd.getValidationRules()
								)
						)
						.toList(),
				createCredentialCmd.getConditions().stream()
						.map(createConditionCmd -> new ChecklistEntry(createConditionCmd.getLabel()))
						.toList()
		);

		credentialRepository.save(credential);
	}

	@Override
	public void delete(final Long id) {
		final Optional<InternalCredential> optionalCredential = credentialRepository.findById(id);

		if (optionalCredential.isEmpty()) {
			return;
		}

		final InternalCredential credential = optionalCredential.get();

		credential.setCategory(null);

		for (final Issuer issuer : credential.getIssuer()) {
			issuer.setCredentials(
					issuer.getCredentials()
							.stream()
							.filter(credential1 -> !credential1.getId().equals(credential.getId()))
							.collect(Collectors.toList())
			);
		}

		credential.setIssuer(null);

		credentialRepository.deleteById(id);
	}

	@Override
	public void update(final Long id, final UpdateCredentialCmd updateCredentialCmd) {
		final Optional<InternalCredential> optionalCredential = credentialRepository.findById(id);

		if (optionalCredential.isEmpty()) {
			return;
		}

		final InternalCredential credential = optionalCredential.get();

		credential.setName(updateCredentialCmd.getName());
		credential.setAgent(updateCredentialCmd.getAgent());
		credential.setCredentialDefinitionId(updateCredentialCmd.getCredentialDefinitionId());

		final List<FormEntry> formEntries = new ArrayList<>();

		for (final UpdateAttributeCmd attribute : updateCredentialCmd.getAttributes()) {
			formEntries.add(
					new FormEntry(
							attribute.getName(),
							attribute.getType(),
							attribute.getAttributeName(),
							attribute.getValidationRules()
					)
			);
		}

		credential.setForm(formEntries);

		final List<ChecklistEntry> checklist = new ArrayList<>();

		for (final UpdateConditionCmd condition : updateCredentialCmd.getConditions()) {
			checklist.add(
					new ChecklistEntry(
							condition.getLabel()
					)
			);
		}

		credential.setChecklist(checklist);

		credentialRepository.save(credential);
	}
}
