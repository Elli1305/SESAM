package com.gpse.sesam.domain.location.door.config;

public interface DoorConfigService {
	void getDoorConfigurations();

	void sendProofConfig(String doorId, ProofConfig proofConfig);
}
