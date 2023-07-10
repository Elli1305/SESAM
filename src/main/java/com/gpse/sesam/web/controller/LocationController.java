package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.DeleteLocationServiceImpl;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
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

@RestController
@CrossOrigin
@RequestMapping("/api/locations")
public class LocationController {

	private final LocationService locationService;
	private final DeleteLocationServiceImpl deleteLocationService;

	@Autowired
	public LocationController(final LocationService locationService, DeleteLocationServiceImpl deleteLocationService) {
		this.locationService = locationService;
		this.deleteLocationService = deleteLocationService;
	}

	@GetMapping
	public List<Location> getNavigationTreeInfo() {

		return locationService.getLocations();
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
		deleteLocationService.deleteById(id);
	}
}