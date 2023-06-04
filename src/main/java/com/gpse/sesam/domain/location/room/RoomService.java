package com.gpse.sesam.domain.location.room;

public interface RoomService {
	void deleteById(Long id);

	Room save(Room room);

	java.util.List<Room> getRooms();
}
