package com.gpse.sesam.domain.location.door.history;

import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.web.cmd.DoorHistoryDetailsCmd;
import com.gpse.sesam.web.cmd.DoorHistoryTableCmd;

import java.util.List;
import java.util.Optional;

public interface HistoryService {
    List<History> getHistories();

    Optional<History> getHistory(Long id);

    void deleteAll(List<History> histories);

    void deleteById(Long id);

    void saveAll(Iterable<History> histories);

    public List<History> historiesByDoor(Long id);

    public List<DoorHistoryTableCmd> getHistoryForTable();


    List<DoorHistoryTableCmd> getHistoryForTableByDoorId(Long id);

    public List<DoorHistoryDetailsCmd> getHistoryDetails();


    List<DoorHistoryDetailsCmd> getHistoryDetailsByDoorId(Long id);

}
