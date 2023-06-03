package com.gpse.sesam.domain.credential.credentials;

import java.util.List;
import java.util.Optional;

public interface ExternalCredentialService {
    List<ExternalCredential> getExternalCredentials();

    Optional<ExternalCredential> getExternalCredential(Long id);

    void deleteAll();

    void saveAll(Iterable<ExternalCredential> externalCredentials);
}
