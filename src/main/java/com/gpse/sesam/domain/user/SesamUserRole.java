package com.gpse.sesam.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serial;

@Entity
public class SesamUserRole {
	@Serial
	private static final long serialVersionUID = 43L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@JsonIgnore
	private Long id;

	@Column(nullable = false)
	private AttainableRole role;

	@Column(nullable = false)
	private boolean granted;

	protected SesamUserRole() {
	}

	public SesamUserRole(final AttainableRole role) {
		this.role = role;
		this.granted = false;
	}

	public SesamUserRole(final AttainableRole role, final boolean granted) {
		this.role = role;
		this.granted = granted;
	}

	public AttainableRole getRole() {
		return role;
	}

	public void setRole(final AttainableRole role) {
		this.role = role;
	}

	public boolean isGranted() {
		return granted;
	}

	public void setGranted(final boolean granted) {
		this.granted = granted;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public enum AttainableRole {
		ADMINISTRATOR,
		EDITOR,
		ISSUER
	}
}
