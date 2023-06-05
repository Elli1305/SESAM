package com.gpse.sesam.domain.credential.credentials;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpse.sesam.domain.credential.category.Category;
import com.gpse.sesam.domain.credential.issuing.ChecklistEntry;
import com.gpse.sesam.domain.credential.issuing.FormEntry;
import com.gpse.sesam.domain.credential.issuing.FormEntryType;
import com.gpse.sesam.domain.credential.issuing.IssueCredential;
import com.gpse.sesam.domain.credential.issuing.IssueCredentialAttribute;
import com.gpse.sesam.domain.credential.issuing.IssueCredentialRequest;
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

@Service
public class CredentialServiceImpl implements CredentialService {

	private final WebClient client;

	private final ObjectMapper mapper;

	private final IssuerRepository issuerRepository;
	private final CredentialRepository credentialRepository;

	private final ExternalCredentialRepository externalCredentialRepository;

	@Autowired
	public CredentialServiceImpl(final WebClient client, final ObjectMapper mapper,
								 final CredentialRepository credentialRepository, final IssuerRepository issuerRepository,
								 final ExternalCredentialRepository externalCredentialRepository) {
		this.client = client;
		this.mapper = mapper;
		this.issuerRepository = issuerRepository;
		this.credentialRepository = credentialRepository;
		this.externalCredentialRepository = externalCredentialRepository;
	}

	public static CredentialCmd createCredentialCmd(final Category category, final Credential credential) {
		final List<String> externalCred = new ArrayList<>();
		final List<String> issuerName = new ArrayList<>();
		final List<String> issuerRoom = new ArrayList<>();

		for (int i = 0; i < category.getExternalCredentials().size(); i++) {
			externalCred.add(category.getExternalCredentials().get(i).getName());
		}

		for (int i = 0; i < credential.getIssuer().size(); i++) {
			issuerRoom.add(credential.getIssuer().get(i).getRoom().getName());
			issuerName.add(credential.getIssuer().get(i).getFirstName() + " " + credential.getIssuer().get(i)
					.getLastName());
		}

		return new CredentialCmd(category.getName(), credential.getName(), externalCred, issuerName, issuerRoom);
	}

	@Override
	public List<Credential> getCredentials() {
		final List<Credential> credentials = new ArrayList<>();
		credentialRepository.findAll().forEach(credentials::add);
		return credentials;
	}

	@Override
	public List<Credential> getCredentialsByIssuerId(final Long id) {
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
	public List<Credential> getCredentialByCredentialIssuerId(final String id) {
		return credentialRepository.findAllByCredentialDefinitionId(id);
	}


	@Override
	public Optional<Credential> getCredential(final Long id) {
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
		final Credential credential = credentialRepository.findById(id).orElseThrow();

		final Map<Long, IssueCredentialAttributeCmd> attributeCmdMap = attributeCmds.stream()
				.collect(Collectors.toMap(IssueCredentialAttributeCmd::id, Function.identity()));

		final List<IssueCredentialAttribute> attributes = credential.getForm().stream().map(entry -> {
			final IssueCredentialAttributeCmd correspondingAttributeCmd = attributeCmdMap.get(entry.getId());

			if (correspondingAttributeCmd == null) {
				return null;
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
	public void saveAll(final Iterable<Credential> credentials) {
		credentialRepository.saveAll(credentials);
	}


	@Override
	public List<Credential> credentialFindByLocation(final Long id) {
		return credentialRepository.findByLocation(id);
	}

	@Override
	public List<CredentialCmd> getCredentialByLocation(final Long id) {
		final List<Credential> credentials = credentialRepository.findByLocation(id);
		final List<CredentialCmd> cmds = new ArrayList<>();

		for (final Credential credential : credentials) {
			final String categoryName = credential.getCategory().getName();
			final String credentialName = credential.getName();
			final List<String> externalCredentials = new ArrayList<>();
			for (final ExternalCredential externalCredential : credential.getCategory().getExternalCredentials()) {
				final String external = externalCredential.getName();
				externalCredentials.add(external);
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

	@Override
	public void create(final CreateCredentialCmd createCredentialCmd) {
		final Credential credential = new Credential(
				createCredentialCmd.getName(),
				createCredentialCmd.getCredentialDefinitionId(),
				createCredentialCmd.getAgent(),
				createCredentialCmd.getAttributes().stream()
						.map(createAttributeCmd ->
								new FormEntry(
										createAttributeCmd.getName(),
										createAttributeCmd.getType(),
										createAttributeCmd.getAttributeName()
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
		final Optional<Credential> optionalCredential = credentialRepository.findById(id);

		if (optionalCredential.isEmpty()) {
			return;
		}

		final Credential credential = optionalCredential.get();

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
		final Optional<Credential> optionalCredential = credentialRepository.findById(id);

		if (optionalCredential.isEmpty()) {
			return;
		}

		final Credential credential = optionalCredential.get();

		credential.setName(updateCredentialCmd.getName());
		credential.setAgent(updateCredentialCmd.getAgent());
		credential.setCredentialDefinitionId(updateCredentialCmd.getCredentialDefinitionId());

		final List<FormEntry> formEntries = new ArrayList<>();

		for (final UpdateAttributeCmd attribute : updateCredentialCmd.getAttributes()) {
			formEntries.add(
					new FormEntry(
							attribute.getName(),
							attribute.getType(),
							attribute.getAttributeName()
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
