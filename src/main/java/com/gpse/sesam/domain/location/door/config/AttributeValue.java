package com.gpse.sesam.domain.location.door.config;


import jakarta.persistence.*;

@Entity
public class AttributeValue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String name;

	@Column
	private String attValue;

	public AttributeValue() {

	}

	public AttributeValue(final String name, final String value) {
		this.name = name;
		this.attValue = value;
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
		this.attValue = value;
	}
}
