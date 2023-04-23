package com.gpse.sesam.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService{

    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> getLocations() {
        final List<Location> locations = new ArrayList<>();

        locationRepository.findAll().forEach(locations::add);

        return locations;
    }

    @Override
    public Optional<Location> getLocation(final Long id) {
        return locationRepository.findById(id);
    }
}
