package com.gpse.sesam.domain.location.building;

import com.gpse.sesam.domain.location.RoomGroupRepository;
import com.gpse.sesam.domain.location.RoomGroupServiceImpl;
import com.gpse.sesam.domain.location.RoomGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuildingServiceImpl implements BuildingService {

	private final BuildingRepository buildingRepository;

	private final RoomGroupRepository roomGroupRepository;

	private final RoomGroupServiceImpl roomGroupService;

	@Autowired
	public BuildingServiceImpl(final BuildingRepository buildingRepository, RoomGroupRepository roomGroupRepository, RoomGroupServiceImpl roomGroupService) {
		this.buildingRepository = buildingRepository;
		this.roomGroupRepository = roomGroupRepository;
		this.roomGroupService = roomGroupService;
	}

	@Override
	public void deleteById(Long id) {
		Optional<Building> building = buildingRepository.findById(id);
		if (building.isPresent()) {
			java.util.List<RoomGroups> roomGroupsList = roomGroupService.getGroupByBuilding(id);
			for (RoomGroups roomGroups: roomGroupsList) {
				roomGroupRepository.deleteById(roomGroups.getId());
			}
		}
		buildingRepository.deleteById(id);
	}

	@Override
	public Building save(Building building) {
		return buildingRepository.save(building);
	}
}
