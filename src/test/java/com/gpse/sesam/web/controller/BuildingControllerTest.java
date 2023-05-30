package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.building.Building;
import com.gpse.sesam.domain.location.building.BuildingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BuildingControllerTest {

	@Mock
	private BuildingService buildingService;

	@InjectMocks
	private BuildingController buildingController;

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
	void saveShouldCallServiceWithCorrectArguments() {
		Building newBuilding = new Building("Test", Collections.emptyList());

		when(buildingService.save(newBuilding)).thenReturn(newBuilding);

		Building buildingSaved = buildingController.save(newBuilding);

		assertThat(buildingSaved.getName(), is(newBuilding.getName()));
		assertThat(buildingSaved.getFloors(), is(newBuilding.getFloors()));
	}

	@Test
	void deleteShouldCallServiceWithCorrectArguments() {
		buildingController.deleteById(1L);

		verify(buildingService).deleteById(1L);
	}
}
