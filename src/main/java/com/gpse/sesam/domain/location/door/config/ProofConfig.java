package com.gpse.sesam.domain.location.door.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.util.Map;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProofConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String description;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "config_attribute_mapping",
			joinColumns = {@JoinColumn(name = "config_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "attribute_name", referencedColumnName = "name")})
	@MapKey(name = "name")
	private Map<String, ProofAttributeInfo> requestedAttributes;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "config_predicate_mapping",
			joinColumns = {@JoinColumn(name = "config_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "predicate_name", referencedColumnName = "name")})
	@MapKey(name = "name")
	private Map<String, ProofPredicateInfo> requestedPredicates;

	protected ProofConfig() {

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Map<String, ProofAttributeInfo> getRequestedAttributes() {
		return requestedAttributes;
	}

	public void setRequestedAttributes(final Map<String, ProofAttributeInfo> requestedAttributes) {
		this.requestedAttributes = requestedAttributes;
	}

	public Map<String, ProofPredicateInfo> getRequestedPredicates() {
		return requestedPredicates;
	}

	public void setRequestedPredicates(final Map<String, ProofPredicateInfo> requestedPredicates) {
		this.requestedPredicates = requestedPredicates;
	}
}
