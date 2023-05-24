package com.gpse.sesam.domain.location;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    List<Location> getLocations();

    Optional<Location> getLocation(Long id);

    Optional<Location> getLocationByName(String name);

	void deleteAll();

	void saveAll(Iterable<Location> locations);
}
