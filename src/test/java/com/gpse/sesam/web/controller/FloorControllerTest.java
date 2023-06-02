package com.gpse.sesam.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.floor.FloorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FloorControllerTest {

	@Mock
	private FloorService floorService;

	@InjectMocks
	private FloorController floorController;

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
	void saveShouldCallServiceWithCorrectArguments() throws JsonProcessingException {
		final Floor newFloor = new Floor(1, "test");
		final MockMultipartFile mockMultipartFile = new MockMultipartFile("test_file", new byte[]{});

		when(floorService.save(newFloor, mockMultipartFile)).thenReturn(newFloor);

		final Floor floorSaved = floorController.save(new ObjectMapper().writer()
				.writeValueAsString(newFloor), mockMultipartFile);

		assertThat(floorSaved.getFloorLevel(), is(newFloor.getFloorLevel()));
		assertThat(floorSaved.getFloorPlanPath(), is(newFloor.getFloorPlanPath()));
		assertThat(floorSaved.getRooms(), is(newFloor.getRooms()));
	}

	@Test
	void deleteShouldCallServiceWithCorrectArguments() {
		floorController.deleteById(1L);

		verify(floorService).deleteById(1L);
	}
}
