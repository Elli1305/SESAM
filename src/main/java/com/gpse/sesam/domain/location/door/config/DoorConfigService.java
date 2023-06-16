package com.gpse.sesam.domain.location.door.config;

import com.gpse.sesam.web.cmd.DoorConfigViewCmd;

public interface DoorConfigService {
	void getDoorConfigurations();

	DoorConfigViewCmd getDoorConfig(String doorApiId);

	void sendProofConfig(String doorId, ProofConfig proofConfig);
}
