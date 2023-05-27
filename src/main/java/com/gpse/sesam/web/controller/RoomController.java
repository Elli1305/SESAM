package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room")
@Secured("EDITOR")
public class RoomController {

	private final RoomService roomService;

	@Autowired
	public RoomController(final RoomService roomService) {
		this.roomService = roomService;
	}

	@PostMapping("/save")
	public Room save(@RequestBody final Room building) {
		return roomService.save(building);
	}

	@DeleteMapping("/{id:\\d+}")
	public void deleteById(@PathVariable("id") final Long id) {
		roomService.deleteById(id);
	}
}
