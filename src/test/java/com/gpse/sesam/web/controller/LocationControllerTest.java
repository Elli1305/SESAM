package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.web.exception.LocationNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LocationControllerTest {

	@Mock
	private LocationService locationService;

	@InjectMocks
	private LocationController locationController;

	private AutoCloseable autoCloseable;

	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}

	@Test
	void getNavigationTreeInfo() {
		locationController.getNavigationTreeInfo();

		verify(locationService).getLocations();
	}

	@Test
	void getLocationShouldReturnContentOfOptional() {
		long id = 1;

		Optional<Location> locationOptional = Optional.of(new Location("Test"));
		when(locationService.getLocation(id)).thenReturn(locationOptional);

		Location location = locationController.getLocationInfo(id);

		assertThat(location, is(locationOptional.get()));
	}

	@Test
	void getLocationShouldRaiseExceptionIfReturnedOptionalIsNull() {
		long id = 1;

		when(locationService.getLocation(id)).thenReturn(Optional.empty());

		assertThrows(LocationNotFoundException.class, () -> locationController.getLocationInfo(id), "Location not " +
				"Found with ID: " + id);

	}
}