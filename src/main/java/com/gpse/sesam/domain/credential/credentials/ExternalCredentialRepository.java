package com.gpse.sesam.domain.credential.credentials;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExternalCredentialRepository extends CrudRepository<ExternalCredential, Long> {

    List<ExternalCredential> findAllByCredentialDefinitionId(String id);

}
