package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class RoomController {

	private final RoomService roomService;

	@Autowired
	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@PostMapping("/room/save")
	public Room save(Room building) {
		return roomService.save(building);
	}

	@DeleteMapping("/room/{id:\\d+}")
	public void deleteById(@PathVariable("id") final Long id) {
		roomService.deleteById(id);
	}
}
