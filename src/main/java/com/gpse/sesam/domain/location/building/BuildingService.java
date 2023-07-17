package com.gpse.sesam.domain.location.building;

public interface BuildingService {
	void deleteById(Long id);

	Building save(Building building);
}
