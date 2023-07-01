package com.gpse.sesam.domain.location.room;

import com.gpse.sesam.domain.location.floor.Floor;

public interface RoomService {
	void deleteById(Long id);

	Room save(Room room);

	java.util.List<Room> getRooms();

	Room getById(Long roomId);

	Floor getFloorToRoom(Long roomId);
}
