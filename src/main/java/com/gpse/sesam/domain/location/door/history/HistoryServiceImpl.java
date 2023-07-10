package com.gpse.sesam.domain.location.door.history;

import com.gpse.sesam.domain.credential.credentials.ExternalCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }


    @Override
    public List<History> getHistories() {
        List<History> histories = new ArrayList<>();
        historyRepository.findAll().forEach(histories::add);
        return histories;
    }

    @Override
    public Optional<History> getHistory(Long id) {
        return historyRepository.findById(id);
    }

    @Override
    public void deleteAll(List<History> histories) {
        historyRepository.deleteAll(histories);
    }

    @Override
    public void deleteById(Long id) {
        historyRepository.deleteById(id);
    }

    @Override
    public void saveAll(Iterable<History> histories) {
        historyRepository.saveAll(histories);
    }
}
