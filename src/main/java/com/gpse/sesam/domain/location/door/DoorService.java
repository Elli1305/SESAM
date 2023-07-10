package com.gpse.sesam.domain.location.door;

import java.util.List;

import java.util.Optional;

public interface DoorService {

	void deleteById(Long id);

	java.util.List<Door> getDoors();

	Door save(Door door);

	List<Door> getAllDoors();

	Optional<Door> findDoorById(Long id);

	Door create(Long roomId, Door door);

	java.util.List<Door> getDoorsByRoomId(Long id);
}
