package com.gpse.sesam.domain.location.door;

import java.util.Optional;

public interface DoorService {

	void deleteById(Long id);

	Door save(Door door);

	Optional<Door> findDoorById(Long id);
}
