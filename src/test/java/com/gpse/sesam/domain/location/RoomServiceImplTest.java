package com.gpse.sesam.domain.location;

import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomRepository;
import com.gpse.sesam.domain.location.room.RoomServiceImpl;
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

class RoomServiceImplTest {

	@Mock
	private RoomRepository roomRepository;

	@InjectMocks
	private RoomServiceImpl roomService;

	@Captor
	private ArgumentCaptor<Room> roomArgumentCaptor;


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
		roomService.deleteById(1L);

		verify(roomRepository).deleteById(idCaptor.capture());
		assertThat(idCaptor.getValue(), is(1L));
	}

	@Test
	void saveShouldCallRepositoryWithCorrectArguments() {
		final Room room = new Room("Test");

		roomService.save(room);

		verify(roomRepository).save(roomArgumentCaptor.capture());
		assertThat(room.getName(), is("Test"));
		assertThat(room.getDoors(), is(Collections.emptyList()));
		assertThat(room.getCoordinates(), is(Collections.emptyList()));
	}

}
