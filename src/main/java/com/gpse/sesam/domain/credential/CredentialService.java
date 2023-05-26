package com.gpse.sesam.domain.credential;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.web.cmd.CreateCredentialCmd;
import com.gpse.sesam.web.cmd.CredentialCmd;
import com.gpse.sesam.web.cmd.IssueCredentialAttributeCmd;

import java.util.List;
import java.util.Optional;

public interface CredentialService {
	List<Credential> getCredentials();

	Optional<Credential> getCredential(Long id);

	String issueCredential(Long id, List<IssueCredentialAttributeCmd> attributeCmds)
			throws JsonProcessingException;


	void saveAll(Iterable<Credential> credentials);

	Optional<Credential> credentialFindByLocation(Location location);

	List<CredentialCmd> credentialFindByLocation(Long id);

	void create(CreateCredentialCmd createCredentialCmd);

	void delete(Long id);
}
