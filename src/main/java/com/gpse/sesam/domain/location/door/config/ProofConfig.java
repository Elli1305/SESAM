package com.gpse.sesam.domain.location.door.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gpse.sesam.domain.location.room.Room;
import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProofConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@JsonIgnore
	private Long id;

	@Column
	private String description;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "config_attribute_mapping",
			joinColumns = {@JoinColumn(name = "config_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "attribute_id", referencedColumnName = "id")})
	@Column
	private Map<String, ProofAttributeInfo> requestedAttributes = new HashMap<>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "config_predicate_mapping",
			joinColumns = {@JoinColumn(name = "config_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "predicate_id", referencedColumnName = "id")})
	@Column
	private Map<String, ProofPredicateInfo> requestedPredicates = new HashMap<>();

	@Column
	private LocalTime startTime;

	@Column
	private LocalTime endTime;

	@Column
	private boolean baseConfig;

	public ProofConfig() {

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

	public void addRequestedPredicate(final String name, final ProofPredicateInfo proofPredicateInfo) {
		requestedPredicates.put(name, proofPredicateInfo);
	}

	public void addRequestedAttributes(final String name, final ProofAttributeInfo proofAttributeInfo) {
		requestedAttributes.put(name, proofAttributeInfo);
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public boolean isBaseConfig() {
		return baseConfig;
	}

	public void setBaseConfig(boolean baseConfig) {
		this.baseConfig = baseConfig;
	}
}
