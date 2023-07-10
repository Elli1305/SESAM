package com.gpse.sesam.domain.location.door;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;

@Service

public class DoorServiceImpl implements DoorService {

    private final DoorRepository doorRepository;

    @Autowired

    public DoorServiceImpl(final DoorRepository doorRepository) {

        this.doorRepository = doorRepository;

    }

    @Override

    public void deleteById(Long id) {

        doorRepository.deleteById(id);

    }

    @Override

    public Door save(Door door) {

        return doorRepository.save(door);

    }

    @Override

    public List<Door> getAllDoors() {
        final List<Door> doors = new ArrayList<>();
        doorRepository.findAll().forEach(doors::add);
        return doors;

    }

}
