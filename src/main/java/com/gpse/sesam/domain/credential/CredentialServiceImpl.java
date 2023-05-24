package com.gpse.sesam.domain.credential;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.web.cmd.IssueCredentialAttributeCmd;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    private String sendCredentialIssueRequest(@Valid IssueCredentialRequest issueCredentialRequest)
            throws JsonProcessingException {
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
    public String issueCredential(final Long id,
                                  final List<IssueCredentialAttributeCmd> attributeCmds)
            throws JsonProcessingException {
        Credential credential = credentialRepository.findById(id).orElseThrow();

        Map<Long, IssueCredentialAttributeCmd> attributeCmdMap =
                attributeCmds.stream().collect(Collectors.toMap(IssueCredentialAttributeCmd::id, Function.identity()));

        List<IssueCredentialAttribute> attributes = credential.getForm().stream()
                .map(entry -> {
                    IssueCredentialAttributeCmd correspondingAttributeCmd = attributeCmdMap.get(entry.getId());

                    if (correspondingAttributeCmd == null) {
                        return null;
                    }

                    return new IssueCredentialAttribute(
                            entry.getAttributeName(),
                            entry.getType() == FormEntryType.DATE
                                    ? correspondingAttributeCmd.value().replace("-", "")
                                    : correspondingAttributeCmd.value(),
                            entry.getType()
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (credential.getForm().size() != attributes.size()) {
            return null;
        }

        return sendCredentialIssueRequest(
                new IssueCredentialRequest(
                        credential.getAgent(),
                        new IssueCredential(
                                credential.getCredentialDefinitionId(),
                                attributes
                        )
                )
        );
    }

    @Override
    public void deleteAll() {
        credentialRepository.deleteAll();
    }

    @Override
    public void saveAll(Iterable<Credential> credentials) {
        credentialRepository.saveAll(credentials);
    }

    @Override
    public Optional<Credential> credentialFindByLocation(Location location) {
        return Optional.empty();
    }


    @Override
    public List<Credential> credentialFindByLocation(Long id) {
        return credentialRepository.findByLocation(id);
    }
}
