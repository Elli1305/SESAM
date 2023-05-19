package com.gpse.sesam.domain.location;

import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.floor.FloorRepository;
import com.gpse.sesam.domain.location.floor.FloorServiceImpl;
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

class FloorServiceImplTest {

	@Mock
	private FloorRepository floorRepository;

	@InjectMocks
	private FloorServiceImpl floorService;

	@Captor
	private ArgumentCaptor<Floor> floorArgumentCaptor;


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
		floorService.deleteById(1L);

		verify(floorRepository).deleteById(idCaptor.capture());
		assertThat(idCaptor.getValue(), is(1L));
	}

	@Test
	void saveShouldCallRepositoryWithCorrectArguments() {
		Floor floor = new Floor(1, "Test", Collections.emptyList());

		floorService.save(floor);

		verify(floorRepository).save(floorArgumentCaptor.capture());
		assertThat(floor.getFloorLevel(), is(1));
		assertThat(floor.getFloorPlanPath(), is("Test"));
		assertThat(floor.getRooms(), is(Collections.emptyList()));
	}

}
