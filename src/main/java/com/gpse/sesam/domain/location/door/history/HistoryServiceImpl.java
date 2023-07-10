package com.gpse.sesam.domain.location.door.history;

import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.DoorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    private final DoorService doorService;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository, DoorService doorService) {
        this.historyRepository = historyRepository;
        this.doorService = doorService;
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

    @Override
    public List<History> historiesByDoor(Long id) {
        List<History> allHistories = getHistories();
        List<History> histories = new ArrayList<>();
        Optional<Door> door = doorService.findDoorById(id);

        for (History history: allHistories) {
            if (history.getDoor().equals(door)) {
                histories.add(history);
            }
        }
        return histories;
    }


}
