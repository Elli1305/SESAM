package com.gpse.sesam.domain.location.door.config;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class TwoWayDoorConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@OneToOne
	private ProofConfig proofConfigIn;

	@OneToOne
	private ProofConfig proofConfigOut;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProofConfig getProofConfigIn() {
		return proofConfigIn;
	}

	public void setProofConfigIn(ProofConfig proofConfigIn) {
		this.proofConfigIn = proofConfigIn;
	}

	public ProofConfig getProofConfigOut() {
		return proofConfigOut;
	}

	public void setProofConfigOut(ProofConfig proofConfigOut) {
		this.proofConfigOut = proofConfigOut;
	}
}
