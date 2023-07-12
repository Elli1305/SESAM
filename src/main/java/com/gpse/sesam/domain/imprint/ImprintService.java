package com.gpse.sesam.domain.imprint;

/**
 * This interface represents a Service for the {@link Imprint} entity.
 * It provides methods for creating, retrieving, and deleting Imprint entries.
 */
public interface ImprintService {

	/**
	 * Creates a new Imprint entry with the provided content.
	 *
	 * @param content the content of the new Imprint entry
	 */
	void createImprintEntry(String content);

	/**
	 * Retrieves the latest Imprint entry.
	 *
	 * @return the content of the latest Imprint entry
	 */
	String getLatestImprintEntry();

	/**
	 * Deletes the Imprint entry.
	 */
	void deleteImprintEntry();
}
