package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.Coordinate;
import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.DoorService;
import com.gpse.sesam.util.DoorCmdMapper;
import com.gpse.sesam.web.cmd.DoorCmd;
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

class DoorControllerTest {

	@Mock
	private DoorService doorService;

	@InjectMocks
	private DoorController doorController;

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

		Door door = new Door("Test", Collections.emptyList());
		when(doorService.save(door)).thenReturn(door);

		DoorCmd doorCmd = new DoorCmd("Test", Collections.emptyList());
		Door doorSaved = doorController.save(doorCmd);

		assertThat(doorSaved.getName(), is(doorCmd.getName()));
		assertThat(doorSaved.getCoordinates(), is(doorCmd.getCoordinates()));
	}

	@Test
	void deleteShouldCallServiceWithCorrectArguments() {
		doorController.deleteById(1L);

		verify(doorService).deleteById(1L);
	}
}
