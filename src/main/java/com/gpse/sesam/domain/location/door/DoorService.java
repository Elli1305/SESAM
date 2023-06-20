package com.gpse.sesam.domain.location.door;

public interface DoorService {

	void deleteById(Long id);

	Door save(Door door);

	Door create(Long roomId, Door door);
}
