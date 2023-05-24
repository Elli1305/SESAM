package com.gpse.sesam.domain.imprint;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ImprintServiceImpl implements ImprintService {

	@Autowired
	private ImprintRepository imprintRepository;

	@Override
	public void createImprintEntry(final String content) {
		final Imprint newEntry = new Imprint(content, LocalDateTime.now());
		imprintRepository.save(newEntry);
	}

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

	@Override
	public void deleteImprintEntry() {
		imprintRepository.deleteAll();
	}
}
