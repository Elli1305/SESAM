package com.gpse.sesam.domain.imprint;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

/**
 * This is an entity class representing the Imprint table in the database.
 */
@Entity
public class Imprint {

	/**
	 * Primary Key - ID of the Imprint.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	/**
	 * The content of the Imprint.
	 */
	@Column(nullable = false)
	private String content;

	/**
	 * The timestamp when the Imprint was created.
	 */
	@Column(nullable = false)
	private LocalDateTime timestamp;

	/**
	 * No argument constructor.
	 */
	protected Imprint() {

	}

	/**
	 * Parameterized constructor to create a new Imprint.
	 *
	 * @param content   The content of the Imprint
	 * @param timestamp The timestamp when the Imprint was created
	 */
	public Imprint(final String content, final LocalDateTime timestamp) {
		this.content = content;
		this.timestamp = timestamp;
	}


	/**
	 * Gets the id of this Imprint.
	 *
	 * @return the id of this Imprint.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets the content of this Imprint.
	 *
	 * @return the content of this Imprint.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Gets the timestamp of this Imprint.
	 *
	 * @return the timestamp of this Imprint.
	 */
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the id of this Imprint.
	 *
	 * @param id the new id of this Imprint.
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Sets the content of this Imprint.
	 *
	 * @param content the new content of this Imprint.
	 */
	public void setContent(final String content) {
		this.content = content;
	}

	/**
	 * Sets the timestamp of this Imprint.
	 *
	 * @param timestamp the new timestamp of this Imprint.
	 */
	public void setTimestamp(final LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
