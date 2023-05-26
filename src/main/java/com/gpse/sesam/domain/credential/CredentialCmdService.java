package com.gpse.sesam.domain.credential;

import com.gpse.sesam.web.cmd.CredentialCmd;

public interface CredentialCmdService {

	void saveAll(Iterable<CredentialCmd> credentials);
}
