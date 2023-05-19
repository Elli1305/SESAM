package com.gpse.sesam.domain.location;

import java.util.List;
import java.util.Optional;

public interface LocationService {
	List<Location> getLocations();

	Optional<Location> getLocation(Long id);

	void deleteAll();

	void saveAll(Iterable<Location> locations);

	Location save(Location location);

	void deleteById(Long id);
}
