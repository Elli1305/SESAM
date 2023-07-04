package com.gpse.sesam.domain.location.room;

import com.gpse.sesam.domain.location.floor.Floor;
import java.util.Optional;

public interface RoomService {
	void deleteById(Long id);

	Room save(Room room);

    java.util.List<Room> getRooms();

    Optional<Room> getRoomById(Long id);

	Room getById(Long roomId);

	Floor getFloorToRoom(Long roomId);
}
