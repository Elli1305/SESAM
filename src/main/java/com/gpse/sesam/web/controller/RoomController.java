package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room")
public class RoomController {

	private final RoomService roomService;

	@Autowired
	public RoomController(final RoomService roomService) {
		this.roomService = roomService;
	}

	@Secured("EDITOR")
	@PostMapping("/save")
	public Room save(@RequestBody final Room building) {
		return roomService.save(building);
	}

	@Secured("EDITOR")
	@DeleteMapping("/{id:\\d+}")
	public void deleteById(@PathVariable("id") final Long id) {
		roomService.deleteById(id);
	}


	@GetMapping("/rooms")
	public java.util.List<Room> getRooms() {
		return roomService.getRooms();
	}
}
