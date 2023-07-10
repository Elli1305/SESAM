package com.gpse.sesam.domain.location.door;

import java.util.Optional;

public interface DoorService {

	void deleteById(Long id);

	java.util.List<Door> getDoors();

	Door save(Door door);

	Optional<Door> findDoorById(Long id);

	Door create(Long roomId, Door door);

	java.util.List<Door> getDoorsByRoomId(Long id);
}
