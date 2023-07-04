package com.gpse.sesam.domain.credential.credentials.internal;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CredentialRepository extends CrudRepository<InternalCredential, Long> {
	List<InternalCredential> findAllByCredentialDefinitionId(String credentialDefinitionId);
}

