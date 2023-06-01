package com.gpse.sesam.domain.location.door.config;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ProofAttributeInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String name;

	@Column
	@OneToMany(cascade = CascadeType.ALL)
	private List<AttributeFilter> restrictions;

	protected ProofAttributeInfo() {

	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public List<AttributeFilter> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(final List<AttributeFilter> restrictions) {
		this.restrictions = restrictions;
	}
}
