package com.gpse.sesam.domain.location.door.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gpse.sesam.util.StringToIntSerializer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class ProofPredicateInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@JsonIgnore
	private Long id;


	@Column
	private String name;

	@Column
	private String predicateType;

	@Column
	@JsonSerialize(using = StringToIntSerializer.class)
	private String predicateValue;

	@Column
	@OneToMany(cascade = CascadeType.ALL)
	private List<AttributeFilter> restrictions;

	public ProofPredicateInfo() {

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
