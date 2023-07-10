package com.gpse.sesam.domain.location.door.history;

import com.gpse.sesam.domain.credential.credentials.Credential;

import java.util.List;
import java.util.Optional;

public interface HistoryService {
    List<History> getHistories();

    Optional<History> getHistory(Long id);

    void deleteAll(List<History> histories);

    void deleteById(Long id);

    void saveAll(Iterable<History> histories);
}
