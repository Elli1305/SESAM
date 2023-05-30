package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomService;
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

class RoomControllerTest {

	@Mock
	private RoomService roomService;

	@InjectMocks
	private RoomController roomController;

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
		Room newRoom = new Room("Test", Collections.emptyList());

		when(roomService.save(newRoom)).thenReturn(newRoom);

		Room buildingSaved = roomController.save(newRoom);

		assertThat(buildingSaved.getName(), is(newRoom.getName()));
		assertThat(buildingSaved.getDoors(), is(newRoom.getDoors()));
		assertThat(buildingSaved.getCoordinates(), is(newRoom.getCoordinates()));
	}

	@Test
	void deleteShouldCallServiceWithCorrectArguments() {
		roomController.deleteById(1L);

		verify(roomService).deleteById(1L);
	}
}
