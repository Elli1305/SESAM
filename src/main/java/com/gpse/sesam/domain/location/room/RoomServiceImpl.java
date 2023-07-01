package com.gpse.sesam.domain.location.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

	private final RoomRepository roomRepository;

	@Autowired
	public RoomServiceImpl(final RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	@Override
	public void deleteById(final Long id) {
		roomRepository.deleteById(id);
	}

	@Override
	public Room save(final Room room) {
		return roomRepository.save(room);
	}

	@Override
	public List<Room> getRooms() {
		final List<Room> rooms = new ArrayList<>();
		roomRepository.findAll().forEach(rooms::add);
		return rooms;
	}

	@Override
	public Room getById(final Long id) {
		return roomRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("room with id " + id + " does not exist"));
	}

	@Override
	public Optional<Room> getRoomById(final Long id) {
		return roomRepository.findById(id);
	}


}
