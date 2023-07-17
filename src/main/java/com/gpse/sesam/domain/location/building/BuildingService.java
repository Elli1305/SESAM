package com.gpse.sesam.domain.location.building;

public interface BuildingService {
	void deleteById(final  Long id);

	Building save(final  Building building);
}
