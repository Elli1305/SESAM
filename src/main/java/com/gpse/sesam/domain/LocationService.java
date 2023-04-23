package com.gpse.sesam.domain;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    List<Location> getLocations();

    Optional<Location> getLocation(Long id);
}
