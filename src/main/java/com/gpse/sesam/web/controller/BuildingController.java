package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.Building;
import com.gpse.sesam.domain.location.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class BuildingController {

	private final BuildingService buildingService;

	@Autowired
	public BuildingController(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@PostMapping("/buildings/save")
	public Building save(Building building) {
		return buildingService.save(building);
	}

	@GetMapping("/buildings/{id:\\d+}")
	public void deleteById(@PathVariable("id") final Long id) {
		buildingService.deleteById(id);
	}
}
