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
    private ImprintRepository ImprintRepository;

    public void createImprintEntry(String content) {
        Imprint newEntry = new Imprint(content, LocalDateTime.now());
        ImprintRepository.save(newEntry);
    }

    public String getLatestImprintEntry() {
        List<Imprint> ImprintEntries = StreamSupport.stream(ImprintRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return ImprintEntries.stream()
                .max(Comparator.comparing(Imprint::getTimestamp))
                .map(Imprint::getContent)
                .orElse(null);
    }
    @Override
    public void deleteImprintEntry() {
        ImprintRepository.deleteAll();
    }
}
