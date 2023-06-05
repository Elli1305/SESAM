package com.gpse.sesam.domain.location.door.config;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AttributeValue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@JsonIgnore
	private Long id;

	@Column
	private String name;

	@Column
	private String attValue;

	public AttributeValue() {

	}

	public AttributeValue(final String name, final String value) {
		this.name = name;
		attValue = value;
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

	public String getValue() {
		return attValue;
	}

	public void setValue(final String value) {
		attValue = value;
	}
}
