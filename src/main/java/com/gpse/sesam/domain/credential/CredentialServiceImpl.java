package com.gpse.sesam.domain.credential;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpse.sesam.web.cmd.IssueCredentialAttributeCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private List<IssueCredentialAttribute> createAttributes(final Long id, final List<IssueCredentialAttributeCmd> attributeCmds) {
        final ArrayList<IssueCredentialAttribute> attributes1 = new ArrayList<>();

        attributes1.add(new IssueCredentialAttribute("id", "1"));
        attributes1.add(new IssueCredentialAttribute("first_name", "Alice"));
        attributes1.add(new IssueCredentialAttribute("last_name", "Ananas"));
        attributes1.add(new IssueCredentialAttribute("birth_date", "20000101"));
        attributes1.add(new IssueCredentialAttribute("expiration_date", "20250101"));

        return attributes1;
    }

    @Override
    public String issueCredential(final Long id, final List<IssueCredentialAttributeCmd> attributeCmds) throws JsonProcessingException {
        final IssueCredentialRequest issueCredentialRequest = new IssueCredentialRequest(
                "tlabs",
                new IssueCredential(
                        "$T-MEMBER",
                        createAttributes(id, attributeCmds)
                )
        );

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
