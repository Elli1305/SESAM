package com.gpse.sesam.domain.location;

import com.gpse.sesam.domain.location.building.Building;
import com.gpse.sesam.domain.location.building.BuildingRepository;
import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.floor.FloorRepository;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomRepository;
import com.gpse.sesam.domain.location.roomgroup.RoomGroupRepository;
import com.gpse.sesam.domain.location.roomgroup.RoomGroupServiceImpl;
import com.gpse.sesam.domain.location.roomgroup.RoomGroups;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeleteLocationServiceImpl {
    private final LocationRepository locationRepository;
    private final RoomGroupRepository roomGroupRepository;

    private final RoomGroupServiceImpl roomGroupService;
    private final BuildingRepository buildingRepository;

    private final FloorRepository floorRepository;

    private final RoomRepository roomRepository;

    public DeleteLocationServiceImpl(LocationRepository locationRepository, RoomGroupRepository roomGroupRepository, RoomGroupServiceImpl roomGroupService, BuildingRepository buildingRepository, FloorRepository floorRepository, RoomRepository roomRepository) {
        this.locationRepository = locationRepository;
        this.roomGroupRepository = roomGroupRepository;
        this.roomGroupService = roomGroupService;
        this.buildingRepository = buildingRepository;
        this.floorRepository = floorRepository;
        this.roomRepository = roomRepository;
    }

    public void deleteById(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            List<Building> buildings = location.get().getBuildings();
            for (Building building : buildings) {
                java.util.List<RoomGroups> roomGroupsList = roomGroupService.getGroupByBuilding(building.getId());
                for (RoomGroups roomGroups: roomGroupsList) {
                    roomGroupRepository.deleteById(roomGroups.getId());
                }
            }
        }
        locationRepository.deleteById(id);
    }

    public void buildingDeleteById(Long id) {
        Optional<Building> building = buildingRepository.findById(id);
        if (building.isPresent()) {
            java.util.List<RoomGroups> roomGroupsList = roomGroupService.getGroupByBuilding(id);
            for (RoomGroups roomGroups: roomGroupsList) {
                roomGroupRepository.deleteById(roomGroups.getId());
            }
        }
        buildingRepository.deleteById(id);
    }

    public void floorDeleteById(Long id) {
        Optional<Floor> floor = floorRepository.findById(id);
        if (floor.isPresent()) {
            List<Room> floorRooms = floor.get().getRooms();
            for (Room room : floorRooms) {
                roomRepository.deleteById(room.getId());
            }
        }
        floorRepository.deleteById(id);
    }
}
