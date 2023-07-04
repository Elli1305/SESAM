package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.DeleteLocationServiceImpl;
import com.gpse.sesam.domain.location.building.Building;
import com.gpse.sesam.domain.location.building.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/building")
@Secured("EDITOR")
public class BuildingController {

	private final BuildingService buildingService;

	private final DeleteLocationServiceImpl deleteLocationService;

	@Autowired
	public BuildingController(final BuildingService buildingService, DeleteLocationServiceImpl deleteLocationService) {
		this.buildingService = buildingService;
		this.deleteLocationService = deleteLocationService;
	}

	@PostMapping(path = "/save")
	public Building save(@RequestBody final Building building) {
		return buildingService.save(building);
	}

	@DeleteMapping(path = "/{id:\\d+}")
	public void deleteById(@PathVariable("id") final Long id) {
		deleteLocationService.buildingDeleteById(id);
	}
}
