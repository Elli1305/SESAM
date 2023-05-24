package com.gpse.sesam.domain.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class RoomGroupServiceImpl implements RoomGroupService {

    private final RoomGroupRepository roomGroupRepository;

    @Autowired
    public RoomGroupServiceImpl(final RoomGroupRepository roomGroupRepository) {
        this.roomGroupRepository = roomGroupRepository;
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
    public void deleteAll() {
        roomGroupRepository.deleteAll();
    }
    @Override
    public void saveAll(final Iterable<RoomGroups> roomGroups) {
        roomGroupRepository.saveAll(roomGroups);
    }
}
