package com.gpse.sesam.domain.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingServiceImpl implements BuildingService {

	private final BuildingRepository buildingRepository;

	@Autowired
	public BuildingServiceImpl(final BuildingRepository buildingRepository) {
		this.buildingRepository = buildingRepository;
	}

	@Override
	public void deleteById(Long id) {
		buildingRepository.deleteById(id);
	}

	@Override
	public Building save(Building building) {
		return buildingRepository.save(building);
	}
}
