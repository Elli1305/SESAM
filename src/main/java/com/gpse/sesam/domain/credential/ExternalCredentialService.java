package com.gpse.sesam.domain.credential;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.web.cmd.IssueCredentialAttributeCmd;

import java.util.List;
import java.util.Optional;

public interface ExternalCredentialService {
    List<ExternalCredential> getExternalCredentials();

    Optional<ExternalCredential> getExternalCredential(Long id);

    void deleteAll();

    void saveAll(Iterable<ExternalCredential> externalCredentials);

    void updateExternalCredential(ExternalCredential externalCredential, String name, String defintionId);

    void deleteExternalCredential(final ExternalCredential externalCredential);

    ExternalCredential findExternalById(Long id);
}
