package com.gpse.sesam.domain.location;

import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.DoorRepository;
import com.gpse.sesam.domain.location.door.DoorServiceImpl;
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

class DoorServiceImplTest {

	@Mock
	private DoorRepository doorRepository;

	@InjectMocks
	private DoorServiceImpl doorService;

	@Captor
	private ArgumentCaptor<Door> buildingCaptor;


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
		doorService.deleteById(1L);

		verify(doorRepository).deleteById(idCaptor.capture());
		assertThat(idCaptor.getValue(), is(1L));
	}

	@Test
	void saveShouldCallRepositoryWithCorrectArguments() {
		Door door = new Door("Test", Collections.emptyList());

		doorService.save(door);

		verify(doorRepository).save(buildingCaptor.capture());
		assertThat(door.getName(), is("Test"));
		assertThat(door.getCoordinates(), is(Collections.emptyList()));
	}

}
