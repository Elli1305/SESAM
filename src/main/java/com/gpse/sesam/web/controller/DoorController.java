package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.DoorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class DoorController {

	private final DoorService doorService;

	@Autowired
	public DoorController(DoorService doorService) {
		this.doorService = doorService;
	}

	@PostMapping("/door/save")
	public Door save(Door door) {
		return doorService.save(door);
	}

	@DeleteMapping("/door/{id:\\d+}")
	public void deleteById(@PathVariable("id") final Long id) {
		doorService.deleteById(id);
	}
}
