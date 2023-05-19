package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.floor.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
@Secured("EDITOR")
public class FloorController {

	private final FloorService floorService;

	@Autowired
	public FloorController(FloorService floorService) {
		this.floorService = floorService;
	}

	@PostMapping("/floor/save")
	public Floor save(Floor floor) {
		return floorService.save(floor);
	}

	@DeleteMapping("/floor/{id:\\d+}")
	public void deleteById(@PathVariable("id") final Long id) {
		floorService.deleteById(id);
	}
}
