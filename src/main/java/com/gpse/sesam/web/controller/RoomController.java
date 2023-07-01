package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.floor.FloorService;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
	public Room save(@RequestBody final Room room) {
		return roomService.save(room);
	}

	@Secured("EDITOR")
	@DeleteMapping("/{id:\\d+}")
	public void deleteById(@PathVariable("id") final Long id) {
		roomService.deleteById(id);
	}


	@GetMapping("/rooms")
	public List<Room> getRooms() {
		return roomService.getRooms();
	}

	@Secured("EDITOR")
	@GetMapping("/floor/{id:\\d+}")
	public Floor getFloorToRoom(@PathVariable("id") final Long id) {
		return roomService.getFloorToRoom(id);
	}
}
