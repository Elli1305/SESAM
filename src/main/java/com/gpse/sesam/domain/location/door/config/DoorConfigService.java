package com.gpse.sesam.domain.location.door.config;

import com.gpse.sesam.web.cmd.DoorConfigCmd;

public interface DoorConfigService {
	void getDoorConfigurations();

	DoorConfigCmd getDoorConfig(String doorApiId);

	void sendProofConfig(String doorId, ProofConfig proofConfig);
}
