package com.gpse.sesam.domain.credential;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.user.Issuer;
import com.gpse.sesam.domain.user.IssuerRepository;
import com.gpse.sesam.web.cmd.CredentialCmd;
import com.gpse.sesam.web.cmd.IssueCredentialAttributeCmd;
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

	@Autowired
	public CredentialServiceImpl(final WebClient client, final ObjectMapper mapper,
								 final IssuerRepository issuerRepository,
								 final CredentialRepository credentialRepository) {
		this.client = client;
		this.mapper = mapper;
		this.issuerRepository = issuerRepository;
		this.credentialRepository = credentialRepository;
	}

	@Override
	public List<Credential> getCredentials() {
		final List<Credential> credentials = new ArrayList<>();
		credentialRepository.findAll().forEach(credentials::add);
		return credentials;
	}

	@Override
	public List<Credential> getCredentialsByIssuerId(final Long id) {
		final Issuer issuer = issuerRepository.findById(id).orElseThrow();
		return issuer.getCredentials();
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


	public List<CredentialCmd> getCredentialByLocation(final Long id) {
		List<Credential> credentials = credentialRepository.findByLocation(id);
		List<CredentialCmd> cmds = new ArrayList<>();

		for (Credential credential : credentials) {
			String categoryName = credential.getCategory().getName();
			String credentialName = credential.getName();
			List<String> externalCredentials = new ArrayList<>();
			for (ExternalCredential externalCredential : credential.getCategory().getExternalCredentials()) {
				String external = externalCredential.getName();
				externalCredentials.add(external);
			}
			List<String> issuers = new ArrayList<>();
			List<String> rooms = new ArrayList<>();
			for (Issuer issuer: credential.getIssuer()) {
				String issuerName = issuer.getFirstName()+ " " + issuer.getLastName();
				String room = issuer.getRoom().getName();
				issuers.add(issuerName);
				rooms.add(room);
			}
			cmds.add(new CredentialCmd(categoryName, credentialName, externalCredentials, issuers, rooms));
		}

		return cmds;
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
}
