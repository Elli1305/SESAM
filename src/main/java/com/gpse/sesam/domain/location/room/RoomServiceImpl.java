package com.gpse.sesam.domain.location.room;

import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.floor.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

	private final RoomRepository roomRepository;
	private final FloorService floorService;

	@Autowired
	public RoomServiceImpl(final RoomRepository roomRepository, final FloorService floorService) {
		this.roomRepository = roomRepository;
		this.floorService = floorService;
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
	@Override
	public Floor getFloorToRoom(Long roomId) {
		Floor theFloor = new Floor(0, "");

		List<Floor> allFloors = floorService.getAll();


		for (Floor floor : allFloors) {
			List<Room> roomsForFloor = floor.getRooms();
			for (Room rooms : roomsForFloor) {
				if (Objects.equals(rooms.getId(), roomId)) {

					theFloor.setFloorLevel(floor.getFloorLevel());
					theFloor.setFloorPlanPath(floor.getFloorPlanPath());

				}
			}
		}
		return theFloor;
	}


}
