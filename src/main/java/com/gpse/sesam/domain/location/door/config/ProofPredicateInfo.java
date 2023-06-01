package com.gpse.sesam.domain.location.door.config;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ProofPredicateInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String name;

	@Column
	private String predicateType;

	@Column
	private String predicateValue;

	@Column
	@OneToMany(cascade = CascadeType.ALL)
	private List<AttributeFilter> restrictions;

	protected ProofPredicateInfo() {

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

	public String getPredicateType() {
		return predicateType;
	}

	public void setPredicateType(final String predicateType) {
		this.predicateType = predicateType;
	}

	public String getPredicateValue() {
		return predicateValue;
	}

	public void setPredicateValue(final String predicateValue) {
		this.predicateValue = predicateValue;
	}

	public List<AttributeFilter> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(final List<AttributeFilter> restrictions) {
		this.restrictions = restrictions;
	}
}
