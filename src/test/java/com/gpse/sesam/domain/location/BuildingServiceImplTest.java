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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;

class BuildingServiceImplTest {

	@Mock
	private BuildingRepository buildingRepository;

	@InjectMocks
	private BuildingServiceImpl buildingService;

	@Captor
	private ArgumentCaptor<Building> buildingCaptor;


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
	void deleteShouldCallRepositoryWithCorrectArguments() {
		buildingService.deleteById(1L);

		verify(buildingRepository).deleteById(idCaptor.capture());
		assertThat(idCaptor.getValue(), is(1L));
	}

	@Test
	void saveShouldCallRepositoryWithCorrectArguments() {
		Building building = new Building("Test", Collections.emptyList());

		buildingService.save(building);

		verify(buildingRepository).save(buildingCaptor.capture());
		assertThat(building.getName(), is("Test"));
		assertThat(building.getFloors(), is(Collections.emptyList()));
	}

}
