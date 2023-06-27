package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.util.ConfigCmdMapper;
import com.gpse.sesam.util.LocationCmdMapper;
import com.gpse.sesam.web.cmd.LocationCmd;
import com.gpse.sesam.web.exception.LocationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/locations")
public class LocationController {

	private final LocationService locationService;

	private final LocationCmdMapper locationCmdMapper;

	@Autowired
	public LocationController(final LocationService locationService, ConfigCmdMapper configCmdMapper) {
		locationCmdMapper = new LocationCmdMapper(configCmdMapper);
		this.locationService = locationService;
	}

	@GetMapping
	public List<LocationCmd> getNavigationTreeInfo() {

		return locationService
				.getLocations()
				.stream()
				.map(locationCmdMapper::toCmd)
				.collect(Collectors.toList());
	}

	@GetMapping("/{id:\\d+}")
	public Location getLocationInfo(@PathVariable("id") final Long id) {
		final Optional<Location> location = locationService.getLocation(id);
		if (location.isPresent()) {
			return location.get();
		} else {
			throw new LocationNotFoundException("Location not Found with ID: " + id);
		}
	}

	@PostMapping("/save")
	@Secured("EDITOR")
	public Location save(@RequestBody final Location location) {
		return locationService.save(location);
	}

	@DeleteMapping("/{id:\\d+}")
	@Secured("EDITOR")
	public void deleteById(@PathVariable("id") final Long id) {
		locationService.deleteById(id);
	}
}