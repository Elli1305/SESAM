package com.gpse.sesam.web;

import com.gpse.sesam.domain.Location;
import com.gpse.sesam.domain.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/locations")
    public List<Location> getNavigationTreeInfo() {

        return locationService.getLocations();
    }

    @GetMapping("/locations/{id:\\d+}")
    public Location getLocationInfo(@PathVariable("id") final Long id) {
        if (locationService.getLocation(id).isPresent()) {
            return locationService.getLocation(id).get();
        } else {
            throw new LocationNotFoundException("Location not Found with ID: " + id);
        }
    }
}