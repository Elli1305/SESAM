package com.gpse.sesam.domain.location;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LocationServiceImplTest {

	@Mock
	private LocationRepository locationRepository;

	@InjectMocks
	private LocationServiceImpl locationService;

	@Captor
	private ArgumentCaptor<List<Location>> locationsCaptor;

	@Captor
	private ArgumentCaptor<Long> idCaptor;

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
	void getLocationsShouldReturnListOfLocations() {
		// arrange

		when(locationRepository.findAll()).thenReturn(Collections.singletonList(new Location()));

		// act
		final List<Location> locations = locationService.getLocations();

		// assert
		assertThat(locations.size(), is(1));
	}

	@Test
	void saveAllShouldCallRepositoryWithCorrectArguments() {
		// arrange
		final List<Location> locations = Collections.singletonList(new Location());

		// act
		locationService.saveAll(locations);

		// assert
		verify(locationRepository).saveAll(locationsCaptor.capture());

		assertThat(locationsCaptor.getValue(), is(locations));
	}

	@Test
	void getLocationShouldCallRepositoryWithCorrectArguments() {
		// arrange
		final long id = 1;

		// act
		locationService.getLocation(id);

		//assert
		verify(locationRepository).findById(idCaptor.capture());
		assertThat(idCaptor.getValue(), is(id));
	}

}