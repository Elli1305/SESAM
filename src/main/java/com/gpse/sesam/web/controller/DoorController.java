package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.DoorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/door")
@Secured("EDITOR")
public class DoorController {

	private final DoorService doorService;

	@Autowired
	public DoorController(final DoorService doorService) {
		this.doorService = doorService;
	}

	@PostMapping("/save")
	public Door save(@RequestBody final Door door) {
		return doorService.save(door);
	}

	@DeleteMapping("/{id:\\d+}")
	public void deleteById(@PathVariable("id") final Long id) {
		doorService.deleteById(id);
	}
}
