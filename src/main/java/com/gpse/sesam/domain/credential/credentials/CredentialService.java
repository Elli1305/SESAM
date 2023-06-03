package com.gpse.sesam.domain.credential.credentials;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.web.cmd.IssueCredentialAttributeCmd;

import java.util.List;
import java.util.Optional;

public interface CredentialService {
	List<Credential> getCredentials();

	List<Credential> getCredentialsByIssuerId(Long di);

	Optional<Credential> getCredential(Long id);

	String issueCredential(Long id, List<IssueCredentialAttributeCmd> attributeCmds)
			throws JsonProcessingException;


	void saveAll(Iterable<Credential> credentials);

	List<Credential> credentialFindByLocation(Long id);

}
