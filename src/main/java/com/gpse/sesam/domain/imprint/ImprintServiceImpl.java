package com.gpse.sesam.domain.imprint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Diese Klasse implementiert den ImprintService, der für das Erstellen, Abrufen und Löschen von Imprint-Einträgen
 * verantwortlich ist.
 */
@Service
public class ImprintServiceImpl implements ImprintService {

	/**
	 * The repository for the Imprint entity.
	 */
	@Autowired
	private ImprintRepository imprintRepository;


	/**
	 * Erstellt einen neuen Imprint-Eintrag mit dem angegebenen Inhalt und speichert ihn in der Datenbank.
	 *
	 * @param content der Inhalt des Imprint-Eintrags
	 */
	@Override
	public void createImprintEntry(final String content) {
		final Imprint newEntry = new Imprint(content, LocalDateTime.now());
		imprintRepository.save(newEntry);
	}

	/**
	 * Ruft den neuesten Imprint-Eintrag aus der Datenbank ab.
	 *
	 * @return der Inhalt des neuesten Imprint-Eintrags oder null, wenn kein Eintrag vorhanden ist
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
	 * Löscht alle Imprint-Einträge aus der Datenbank.
	 */
	@Override
	public void deleteImprintEntry() {
		imprintRepository.deleteAll();
	}
}
