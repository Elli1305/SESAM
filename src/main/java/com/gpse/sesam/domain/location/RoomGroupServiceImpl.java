package com.gpse.sesam.domain.location;

import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.web.cmd.RoomGroupCmd;
import com.gpse.sesam.web.exception.ConflictException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service

public class RoomGroupServiceImpl implements RoomGroupService {

    private final RoomGroupRepository roomGroupRepository;

    @Autowired
    public RoomGroupServiceImpl(final RoomGroupRepository roomGroupRepository) {
        this.roomGroupRepository = roomGroupRepository;
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
}
