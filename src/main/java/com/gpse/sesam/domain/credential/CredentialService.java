package com.gpse.sesam.domain.credential;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.web.cmd.IssueCredentialAttributeCmd;

import java.util.List;
import java.util.Optional;

public interface CredentialService {
    List<Credential> getCredentials();

    Optional<Credential> getCredential(Long id);

    String issueCredential(Long id, List<IssueCredentialAttributeCmd> attributeCmds)
            throws JsonProcessingException;

    void deleteAll();

    void saveAll(Iterable<Credential> credentials);

    Optional<Credential> credentialFindByLocation(Location location);

    List<Credential> credentialFindByLocation(Long id);

}
