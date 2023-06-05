package com.gpse.sesam.domain.credential.issuing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FormEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column(nullable = false)
	private String label;

	@Column(nullable = false)
	private FormEntryType type;

	@Column(nullable = false)
	private String attributeName;

	protected FormEntry() {
	}

	public FormEntry(final String label, final FormEntryType type, final String attributeName) {
		this.label = label;
		this.type = type;
		this.attributeName = attributeName;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public FormEntryType getType() {
		return type;
	}

	public void setType(final FormEntryType type) {
		this.type = type;
	}

	public String getAttributeName() {
		return attributeName;
	}
}
