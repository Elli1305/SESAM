package com.gpse.sesam.domain.location.roomgroup;

import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.DoorRepository;
import com.gpse.sesam.domain.location.door.config.*;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.util.ConfigCmdMapper;
import com.gpse.sesam.web.cmd.*;
import com.gpse.sesam.web.exception.ConflictException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Diese Klasse implementiert den RoomGroupService und stellt Funktionen zur Verwaltung von Raumgruppen bereit.
 */
@Service
public class RoomGroupServiceImpl implements RoomGroupService {

    private final RoomGroupRepository roomGroupRepository;
    private final DoorConfigService doorConfigurationService;

    private final DoorRepository doorRepository;


    /**
     * Konstruktor für RoomGroupServiceImpl.
     *
     * @param roomGroupRepository       das RoomGroupRepository zur Datenbankabfrage von Raumgruppen
     * @param doorConfigurationService  der DoorConfigService zur Verwaltung der Türkonfiguration
     * @param doorRepository            das DoorRepository zur Datenbankabfrage von Türen
     */
    @Autowired
    public RoomGroupServiceImpl(final RoomGroupRepository roomGroupRepository,
                                DoorConfigService doorConfigurationService,
                                DoorRepository doorRepository) {
        this.roomGroupRepository = roomGroupRepository;
        this.doorConfigurationService = doorConfigurationService;
        this.doorRepository = doorRepository;
    }

    /**
     * Erstellt eine neue Raumgruppe anhand der angegebenen Raumgruppen-Daten.
     *
     * @param roomGroupCmd die Raumgruppen-Daten für die zu erstellende Raumgruppe
     * @return die erstellte Raumgruppe
     * @throws ConflictException wenn eine Raumgruppe mit dieser ID bereits existiert
     */
    @Override
    public RoomGroups createRoomGroup(final RoomGroupCmd roomGroupCmd) {
        final RoomGroups newRoomgroup = new RoomGroups(
                roomGroupCmd.getName(),
                roomGroupCmd.getRooms(),
                roomGroupCmd.getBuilding()
        );
        try {
            return roomGroupRepository.save(newRoomgroup);
        } catch (final DataIntegrityViolationException e) {
            throw new ConflictException("A group with this id already exists", e);
        }
    }


    /**
     * Gibt eine Liste aller Raumgruppen zurück.
     *
     * @return eine Liste aller Raumgruppen
     */
    @Override
    public List<RoomGroups> getRoomGroups() {
        final List<RoomGroups> roomGroups = new ArrayList<>();
        roomGroupRepository.findAll().forEach(roomGroups::add);
        return roomGroups;
    }

    /**
     * Gibt eine Liste von Raumgruppen zurück, die zum angegebenen Gebäude gehören.
     *
     * @param bId die ID des Gebäudes
     * @return eine Liste von Raumgruppen, die zum Gebäude gehören
     */
    @Override
    public List<RoomGroups> getGroupByBuilding(Long bId) {
        final List<RoomGroups> allRoomGroups = getRoomGroups();
        List<RoomGroups> filteredGroups = new ArrayList<>();
        for (RoomGroups roomGroup : allRoomGroups) {
            if (Objects.equals(roomGroup.getBuilding().getId(), bId)) {
                filteredGroups.add(roomGroup);
            }
        }
        return filteredGroups;
    }

    /**
     * Ruft eine Raumgruppe anhand der angegebenen ID ab.
     *
     * @param id die ID der abzurufenden Raumgruppe
     * @return Optional, das die gefundene Raumgruppe oder null enthält
     */
    @Override
    public Optional<RoomGroups> getRoomGroups(final Long id) {
        return roomGroupRepository.findById(id);
    }

    /**
     * Ruft eine Raumgruppe anhand der angegebenen ID ab.
     *
     * @param id die ID der abzurufenden Raumgruppe
     * @return die gefundene Raumgruppe
     * @throws EntityNotFoundException wenn keine Raumgruppe mit der angegebenen ID existiert
     */
    @Override
    public RoomGroups getGroupById(Long id) {
        return roomGroupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
    }

    /**
     * Löscht alle Raumgruppen.
     */
    @Override
    public void deleteAll() {
        roomGroupRepository.deleteAll();
    }

    /**
     * Speichert eine Liste von Raumgruppen.
     *
     * @param roomGroups die zu speichernden Raumgruppen
     */
    @Override
    public void saveAll(final Iterable<RoomGroups> roomGroups) {
        roomGroupRepository.saveAll(roomGroups);
    }


    /**
     * Speichert eine Raumgruppe.
     *
     * @param roomGroup die zu speichernde Raumgruppe
     * @return die gespeicherte Raumgruppe
     */
    @Override
    public RoomGroups save(RoomGroups roomGroup) {
        return roomGroupRepository.save(roomGroup);
    }

    /**
     * Aktualisiert den Namen und die Räume einer Raumgruppe.
     *
     * @param editGroup die zu aktualisierende Raumgruppe
     * @param newName   der neue Name der Raumgruppe
     * @param newRooms  die neuen Räume der Raumgruppe
     */
    @Override
    public void makeGroupEdit(final RoomGroups editGroup, final String newName, final List<Room> newRooms) {
        editGroup.setName(newName);
        editGroup.setRooms(newRooms);

        roomGroupRepository.save(editGroup);
    }

    /**
     * Löscht eine Raumgruppe anhand der angegebenen ID.
     *
     * @param id die ID der zu löschenden Raumgruppe
     */
    @Override
    public void deleteById(Long id) {
        roomGroupRepository.deleteById(id);
    }

    /**
     * Setzt die Konfiguration für eine Gruppe von Türen.
     *
     * @param cmds die Liste von TwoWayDoorConfigCmd-Objekten, die die Konfigurationen enthalten
     */
    @Override
    public void setGroupConfig(List<TwoWayDoorConfigCmd> cmds) {
        for (TwoWayDoorConfigCmd cmd : cmds) {
            Optional<Door> door = doorRepository.findById(Long.valueOf(cmd.getDoorConfigIn().getDoorId()));
            // TODO change when merged with master
//            if (door.isPresent()) {
//                doorConfigurationService.sendProofConfig(cmd.getDoorConfigIn()
//                        .getDoorId(), ConfigCmdMapper.fromCmd(cmd.getDoorConfigIn()));
//                doorConfigurationService.sendProofConfig(cmd.getDoorConfigOut()
//                        .getDoorId(), ConfigCmdMapper.fromCmd(cmd.getDoorConfigOut()));
//                door.get().setProofConfigIn(List.of(ConfigCmdMapper.fromCmd(cmd.getDoorConfigIn())));
//                door.get().setProofConfigOut(List.of(ConfigCmdMapper.fromCmd(cmd.getDoorConfigOut())));
//                doorRepository.save(door.get());
//            }
        }
    }

    /**
     * Gibt eine Liste von RoomGroupDoorConfigCmd-Objekten zurück, die die Räume und Türen für die angegebene Raumgruppen-ID enthalten.
     *
     * @param id die ID der Raumgruppe
     * @return eine Liste von RoomGroupDoorConfigCmd-Objekten mit den Räumen und Türen der Raumgruppe
     */
    @Override
    public List<RoomGroupDoorConfigCmd> getRoomsAndDoorsByGroupId(Long id) {
        Optional<RoomGroups> roomGroup = roomGroupRepository.findById(id);
        List<RoomGroupDoorConfigCmd> cmds = new ArrayList<>();
        if (roomGroup.isPresent()) {
            for (Room room: roomGroup.get().getRooms()) {
                java.util.List<Door> doors = new ArrayList<>();
                String roomName = room.getName();
                Long roomId = room.getId();
                for (Door door: room.getDoors()) {
                    doors.add(door);
                }
                cmds.add(new RoomGroupDoorConfigCmd(roomId, roomName, doors));
            }
        }
        return cmds;
    }

    /**
     * Gibt eine Liste von Räumen zurück, die zur angegebenen Raumgruppen-ID gehören.
     *
     * @param id die ID der Raumgruppe
     * @return eine Liste von Räumen, die zur Raumgruppe gehören
     */
    @Override
    public List<Room> getRoomsByGroupId(Long id) {
        Optional<RoomGroups> roomGroup = roomGroupRepository.findById(id);
        List<Room> rooms = new ArrayList<>();
        if (roomGroup.isPresent()) {
            for (Room room: roomGroup.get().getRooms()) {
                rooms.add(room);
            }
        }
        return rooms;
    }
}
