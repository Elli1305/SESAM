package com.gpse.sesam.domain.location.room;
import java.util.ArrayList;

public interface RoomService {
	void deleteById(Long id);

	Room save(Room room);

	java.util.List<Room> getRooms();
}
