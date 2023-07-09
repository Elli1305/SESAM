package com.gpse.sesam.domain.location.door;

import java.util.List;

public interface DoorService {

	void deleteById(Long id);

	Door save(Door door);

	List<Door> getAllDoors();
}
