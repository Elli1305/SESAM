package com.gpse.sesam.domain.location;

import com.gpse.sesam.domain.location.building.Building;
import com.gpse.sesam.web.exception.LocationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {

	private final LocationRepository locationRepository;
	private final RoomGroupRepository roomGroupRepository;

	private final RoomGroupServiceImpl roomGroupService;

	@Autowired
	public LocationServiceImpl(final LocationRepository locationRepository, RoomGroupRepository roomGroupRepository, RoomGroupServiceImpl roomGroupService) {
		this.locationRepository = locationRepository;
		this.roomGroupRepository = roomGroupRepository;
		this.roomGroupService = roomGroupService;
	}

	@Override
	public List<Location> getLocations() {
		final List<Location> locations = new ArrayList<>();

		locationRepository.findAll().forEach(locations::add);

		return locations;
	}

	@Override
	public Optional<Location> getLocation(final Long id) {
		return locationRepository.findById(id);
	}

	@Override
	public Optional<Location> getLocationByName(final String name) throws LocationNotFoundException {
		return Optional.ofNullable(locationRepository.findByName(name)
				.orElseThrow(() -> new LocationNotFoundException(name + "Not found")));
	}

	@Override
	public void saveAll(final Iterable<Location> locations) {
		locationRepository.saveAll(locations);
	}

	@Override
	public Location save(Location location) {
		return locationRepository.save(location);
	}

	@Override
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
}
