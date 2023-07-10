package com.gpse.sesam.domain.imprint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The ImprintServiceImpl class implements the {@link ImprintService} interface.
 * It provides methods for managing the Imprint entries in the database.
 */
@Service
public class ImprintServiceImpl implements ImprintService {

	/**
	 * The repository for the Imprint entity.
	 */
	@Autowired
	private ImprintRepository imprintRepository;

	/**
	 * Creates a new Imprint entry with the provided content.
	 *
	 * @param content the content of the new Imprint entry
	 */
	@Override
	public void createImprintEntry(final String content) {
		final Imprint newEntry = new Imprint(content, LocalDateTime.now());
		imprintRepository.save(newEntry);
	}

	/**
	 * Retrieves the latest Imprint entry.
	 *
	 * @return the content of the latest Imprint entry
	 */
	@Override
	public String getLatestImprintEntry() {
		final List<Imprint> imprintEntries = StreamSupport
				.stream(imprintRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());

		return imprintEntries.stream()
				.max(Comparator.comparing(Imprint::getTimestamp))
				.map(Imprint::getContent)
				.orElse(null);
	}

	/**
	 * Deletes all Imprint entries in the database.
	 */
	@Override
	public void deleteImprintEntry() {
		imprintRepository.deleteAll();
	}
}
