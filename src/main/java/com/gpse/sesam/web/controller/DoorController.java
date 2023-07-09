package com.gpse.sesam.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.DoorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/door")
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


	@GetMapping("/doors")
	public List<Door> getAllDoors() {
		return doorService.getAllDoors();
	}
}
