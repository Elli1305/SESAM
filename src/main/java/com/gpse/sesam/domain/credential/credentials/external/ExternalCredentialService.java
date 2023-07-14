package com.gpse.sesam.domain.credential.credentials.external;

import com.gpse.sesam.web.cmd.CreateExternalCredentialCmd;
import com.gpse.sesam.web.cmd.ExternalCredentialCmd;

import java.util.List;
import java.util.Optional;

public interface ExternalCredentialService {
    List<ExternalCredential> getExternalCredentials();

    Optional<ExternalCredential> getExternalCredential(Long id);

    void createExternalCredential(CreateExternalCredentialCmd createExternalCredentialCmd);

    void deleteExternalCredential(Long id);

    void updateExternalCredential(Long id, CreateExternalCredentialCmd createExternalCredentialCmd);

    void deleteAll();

    void saveAll(Iterable<ExternalCredential> externalCredentials);

    void save(ExternalCredential externalCredential);

    List<ExternalCredential> getExternalCredentialByCredentialDefinitionId(String id);

    List<ExternalCredentialCmd> getAllExternal();

    List<ExternalCredentialCmd> getAllExternalByLocation(Long id);
}
