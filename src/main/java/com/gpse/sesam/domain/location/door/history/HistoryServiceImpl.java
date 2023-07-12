package com.gpse.sesam.domain.location.door.history;

import com.gpse.sesam.domain.credential.credentials.external.ExternalCredential;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredentialService;
import com.gpse.sesam.domain.credential.credentials.internal.CredentialRepository;
import com.gpse.sesam.domain.credential.credentials.internal.InternalCredential;
import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.DoorService;
import com.gpse.sesam.domain.location.door.config.AttributeFilter;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.web.cmd.DoorHistoryDetailsCmd;
import com.gpse.sesam.web.cmd.DoorHistoryTableCmd;
import jakarta.validation.OverridesAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    private final DoorService doorService;
    private final CredentialRepository credentialRepository;

    private final ExternalCredentialService externalCredentialService;


    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository, DoorService doorService, CredentialRepository credentialRepository, ExternalCredentialService externalCredentialService) {
        this.historyRepository = historyRepository;
        this.doorService = doorService;
        this.credentialRepository = credentialRepository;
        this.externalCredentialService = externalCredentialService;
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

    public List<DoorHistoryTableCmd> getHistoryForTable() {
        List<History> histories = getHistories();
        List<DoorHistoryTableCmd> tableHistory = new ArrayList<>();

        for (History history : histories) {
            Date date = history.getDate();
            String doorName = history.getDoor().getName();
            Long id = history.getDoor().getId();
            List<String> credentials = new ArrayList<>();
            List<InternalCredential> internalCredentials = (List<InternalCredential>)
                    getAttachedConfigByDoor(history.getDoor().getId());
            List<ExternalCredential> externalCredentials = (List<ExternalCredential>)
                    getAttachedExternalConfigByDoor(history.getDoor().getId());
            for (ExternalCredential externalCredential : externalCredentials) {
                credentials.add(externalCredential.getName());
            }
            for (InternalCredential internalCredential : internalCredentials) {
                credentials.add(internalCredential.getName());
            }
            tableHistory.add(new DoorHistoryTableCmd(date, id, doorName, credentials));
        }

        return tableHistory;
    }

    @Override
    public List<DoorHistoryTableCmd> getHistoryForTableByDoorId(Long id) {
        List<History> histories = historiesByDoor(id);
        List<DoorHistoryTableCmd> tableHistory = new ArrayList<>();

        for (History history : histories) {
            Date date = history.getDate();
            String doorName = history.getDoor().getName();
            List<String> credentials = new ArrayList<>();
            List<InternalCredential> internalCredentials = (List<InternalCredential>)
                    getAttachedConfigByDoor(history.getDoor().getId());
            List<ExternalCredential> externalCredentials = (List<ExternalCredential>)
                    getAttachedExternalConfigByDoor(history.getDoor().getId());
            for (ExternalCredential externalCredential : externalCredentials) {
                credentials.add(externalCredential.getName());
            }
            for (InternalCredential internalCredential : internalCredentials) {
                credentials.add(internalCredential.getName());
            }
            tableHistory.add(new DoorHistoryTableCmd(date, id, doorName, credentials));
        }

        return tableHistory;
    }

    @Override
    public List<DoorHistoryDetailsCmd> getHistoryDetails() {
        List<DoorHistoryTableCmd> historyTableCmds = getHistoryForTable();
        List<DoorHistoryDetailsCmd> history = new ArrayList<>();
        for (DoorHistoryTableCmd cmd : historyTableCmds) {
            Optional<Door> door = doorService.findDoorById(cmd.getDoorId());
            Room room = doorService.getRoomByDoorId(cmd.getDoorId());
            history.add(new DoorHistoryDetailsCmd(door.get(), room.getName(), cmd.getDate()));

        }
        return history;
    }

    @Override
    public List<DoorHistoryDetailsCmd> getHistoryDetailsByDoorId(Long id) {
        List<DoorHistoryTableCmd> historyTableCmds = getHistoryForTableByDoorId(id);
        List<DoorHistoryDetailsCmd> history = new ArrayList<>();
        for (DoorHistoryTableCmd cmd : historyTableCmds) {
            Optional<Door> door = doorService.findDoorById(cmd.getDoorId());
            Room room = doorService.getRoomByDoorId(cmd.getDoorId());
            history.add(new DoorHistoryDetailsCmd(door.get(), room.getName(), cmd.getDate()));

        }
        return history;
    }

    private Iterable<InternalCredential>getAttachedConfigByDoor(Long id) {
        Optional<Door> door = doorService.findDoorById(id);
        return door.get()
                .getDoorConfigs().stream()
                .flatMap(twoWayDoorConfig -> Stream.of(twoWayDoorConfig.getProofConfigIn(), twoWayDoorConfig.getProofConfigOut()))
                .flatMap(proofConfig -> {
                    final Stream<String> attributeFilterStream = proofConfig.getRequestedPredicates().values()
                            .stream()
                            .flatMap(proofPredicateInfo -> proofPredicateInfo.getRestrictions().stream())
                            .map(AttributeFilter::getCredentialDefinitionId);
                    final Stream<String> attributeFilterStream1 = proofConfig.getRequestedAttributes().values()
                            .stream()
                            .flatMap(proofAttributeInfo -> proofAttributeInfo.getRestrictions().stream())
                            .map(AttributeFilter::getCredentialDefinitionId);
                    return Stream.concat(attributeFilterStream, attributeFilterStream1);
                })
                .filter(Objects::nonNull)
                .flatMap(definitionId -> credentialRepository.findAllByCredentialDefinitionId(definitionId)
                        .stream())
                .collect(Collectors.toSet());
    }

    private Iterable<ExternalCredential>getAttachedExternalConfigByDoor(Long id) {
        Optional<Door> door = doorService.findDoorById(id);
        return door.get()
                .getDoorConfigs().stream()
                .flatMap(twoWayDoorConfig -> Stream.of(twoWayDoorConfig.getProofConfigIn(), twoWayDoorConfig.getProofConfigOut()))
                .flatMap(proofConfig -> {
                    final Stream<String> attributeFilterStream = proofConfig.getRequestedPredicates().values()
                            .stream()
                            .flatMap(proofPredicateInfo -> proofPredicateInfo.getRestrictions().stream())
                            .map(AttributeFilter::getCredentialDefinitionId);
                    final Stream<String> attributeFilterStream1 = proofConfig.getRequestedAttributes().values()
                            .stream()
                            .flatMap(proofAttributeInfo -> proofAttributeInfo.getRestrictions().stream())
                            .map(AttributeFilter::getCredentialDefinitionId);
                    return Stream.concat(attributeFilterStream, attributeFilterStream1);
                })
                .filter(Objects::nonNull)
                .flatMap(definitionId -> externalCredentialService.getExternalCredentialByCredentialDefinitionId(definitionId)
                        .stream())
                .collect(Collectors.toSet());
    }


}
