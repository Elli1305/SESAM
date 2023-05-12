package com.gpse.sesam.domain.credential;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpse.sesam.web.cmd.IssueCredentialAttributeCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.*;

@Service
public class CredentialServiceImpl implements CredentialService {

    private final WebClient client;

    private final ObjectMapper mapper;

    private final CredentialRepository credentialRepository;

    @Autowired
    public CredentialServiceImpl(WebClient client, ObjectMapper mapper, CredentialRepository credentialRepository) {
        this.client = client;
        this.mapper = mapper;
        this.credentialRepository = credentialRepository;
    }

    @Override
    public List<Credential> getCredentials() {
        final List<Credential> credentials = new ArrayList<>();
        credentialRepository.findAll().forEach(credentials::add);
        return credentials;
    }

    @Override
    public Optional<Credential> getCredential(Long id) {
        return credentialRepository.findById(id);
    }

    private IssueCredentialRequest createIssueCredentialRequest(final Long id,
                                                            final List<IssueCredentialAttributeCmd> attributeCmds) {
        final Optional<Credential> optionalCredential = credentialRepository.findById(id);

        if (!optionalCredential.isPresent()) {
            return null;
        }

        final Credential credential = optionalCredential.get();
        final List<FormEntry> form = credential.getForm();

        if (form.size() != attributeCmds.size()) {
            return null;
        }

        final ArrayList<IssueCredentialAttribute> attributes1 = new ArrayList<>();

        Map<Long, IssueCredentialAttributeCmd> attributeCmdMap = new HashMap<>();
        for (IssueCredentialAttributeCmd attributeCmd : attributeCmds) {
            attributeCmdMap.put(attributeCmd.id(), attributeCmd);
        }

        for (final FormEntry entry : form) {
            final IssueCredentialAttributeCmd correspondingAttributeCmd = attributeCmdMap.get(entry.getId());

            if (correspondingAttributeCmd == null) {
                return null;
            }

            switch (entry.getType()) {
                case DATE:
                    attributes1.add(new IssueCredentialAttribute(entry.getAttributeName(), correspondingAttributeCmd.value().replace("-", "")));
                    break;
                default:
                    attributes1.add(new IssueCredentialAttribute(entry.getAttributeName(), correspondingAttributeCmd.value()));
                    break;
            }

        }

        return new IssueCredentialRequest(
                credential.getAgent(),
                new IssueCredential(
                        credential.getCredentialDefinitionId(),
                        attributes1
                )
        );
    }

    @Override
    public String issueCredential(final Long id,
                                  final List<IssueCredentialAttributeCmd> attributeCmds)
            throws JsonProcessingException {
        final IssueCredentialRequest issueCredentialRequest = createIssueCredentialRequest(id, attributeCmds);

        return client.post()
                .uri("credential/issue")
                .contentType(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(issueCredentialRequest))
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(5000))
                .block();
    }

    @Override
    public void deleteAll() {
        credentialRepository.deleteAll();
    }

    @Override
    public void saveAll(Iterable<Credential> credentials) {
        credentialRepository.saveAll(credentials);
    }
}
