package com.gpse.sesam.domain.location;

import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.config.ProofAttributeInfo;
import com.gpse.sesam.domain.location.door.config.ProofConfig;
import com.gpse.sesam.domain.location.door.config.ProofPredicateInfo;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomRepository;
import com.gpse.sesam.domain.location.room.RoomService;
import com.gpse.sesam.domain.location.room.RoomServiceImpl;
import com.gpse.sesam.web.cmd.RoomGroupCmd;
import com.gpse.sesam.web.cmd.RoomGroupConfigCmd;
import com.gpse.sesam.web.exception.ConflictException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.OverridesAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service

public class RoomGroupServiceImpl implements RoomGroupService {

    private final RoomGroupRepository roomGroupRepository;

    private final RoomRepository roomRepository;

    @Autowired
    public RoomGroupServiceImpl(final RoomGroupRepository roomGroupRepository, RoomRepository roomRepository) {
        this.roomGroupRepository = roomGroupRepository;
        this.roomRepository = roomRepository;
    }

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

    @Override
    public List<RoomGroups> getRoomGroups() {
        final List<RoomGroups> roomGroups = new ArrayList<>();
        roomGroupRepository.findAll().forEach(roomGroups::add);
        return roomGroups;
    }

    @Override
    public Optional<RoomGroups> getRoomGroups(final Long id) {
        return roomGroupRepository.findById(id);
    }

    @Override
    public RoomGroups getGroupById(Long id) {
        return roomGroupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
    }

    @Override
    public void deleteAll() {
        roomGroupRepository.deleteAll();
    }

    @Override
    public void saveAll(final Iterable<RoomGroups> roomGroups) {
        roomGroupRepository.saveAll(roomGroups);
    }

    @Override
    public RoomGroups save(RoomGroups roomGroup) {
        return roomGroupRepository.save(roomGroup);
    }

    @Override
    public void makeGroupEdit(final RoomGroups editGroup, final String newName, final List<Room> newRooms) {
        editGroup.setName(newName);
        editGroup.setRooms(newRooms);

        roomGroupRepository.save(editGroup);
    }

    @Override
    public void deleteById(Long id) {
        roomGroupRepository.deleteById(id);
    }

    @Override
    public void setGroupConfig(List<RoomGroupConfigCmd> cmds) {
        for (RoomGroupConfigCmd cmd : cmds) {
            Optional<Room> room = roomRepository.findById(cmd.getRoom());
            if (room.isPresent()) {
                List<Door> doors = room.get().getDoors();
                for (Door door : doors) {
                    for (Long doorId : cmd.getDoors()) {
                        if (door.getId() == doorId) {
                            String description = cmd.getDescription();
                            Map<String, ProofPredicateInfo> requestedPredicates = new HashMap<>();
                            Map<String, ProofAttributeInfo> requestedAttributes = new HashMap<>();
                            ProofConfig config = new ProofConfig();
                            config.setDescription(description);
                            config.setRequestedAttributes(requestedAttributes);
                            config.setRequestedPredicates(requestedPredicates);
                            //door.setConfig(proofConfig);
                        }
                    }
                }
            }
        }
    }
}
