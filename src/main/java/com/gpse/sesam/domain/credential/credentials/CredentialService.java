package com.gpse.sesam.domain.credential.credentials;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.web.cmd.*;

import java.util.List;
import java.util.Optional;

public interface CredentialService {
	List<InternalCredential> getCredentials();

	List<Credential> getAllCredentials();

	List<InternalCredential> getCredentialsByIssuerId(Long di);
	List<ExternalCredential> getExternalCredentials();

	List<Credential> getCredentialByCredentialDefinitionId(String id);

	Optional<InternalCredential> getCredential(Long id);

	String issueCredential(Long id, List<IssueCredentialAttributeCmd> attributeCmds)
			throws JsonProcessingException;


	void saveAll(Iterable<InternalCredential> credentials);

	List<CredentialCmd> getCredentialByLocation(Long id);

	void create(CreateCredentialCmd createCredentialCmd);

	void delete(Long id);

	void update(Long id, UpdateCredentialCmd updateCredentialCmd);

	List<CredentialCmd> getAllCredentialsForView();

	List<AllCredentialCmd> getAllForView();
}
