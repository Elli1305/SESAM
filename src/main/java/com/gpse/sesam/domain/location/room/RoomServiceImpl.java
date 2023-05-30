package com.gpse.sesam.domain.location.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {

	private final RoomRepository roomRepository;

	@Autowired
	public RoomServiceImpl(final RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	@Override
	public void deleteById(Long id) {
		roomRepository.deleteById(id);
	}

	@Override
	public Room save(Room room) {
		return roomRepository.save(room);
	}
}
