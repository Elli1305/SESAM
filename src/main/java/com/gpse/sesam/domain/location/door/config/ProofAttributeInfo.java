package com.gpse.sesam.domain.location.door.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class ProofAttributeInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@JsonIgnore
	private Long id;

	@Column
	private String name;

	@Column
	@OneToMany(cascade = CascadeType.ALL)
	private List<AttributeFilter> restrictions;

	public ProofAttributeInfo() {

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
