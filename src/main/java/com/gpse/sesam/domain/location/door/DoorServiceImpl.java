package com.gpse.sesam.domain.location.door;

import com.gpse.sesam.domain.location.door.config.DoorConfigService;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoorServiceImpl implements DoorService {

	private final DoorRepository doorRepository;

	private final RoomService roomService;

	private final DoorConfigService doorConfigService;

	@Autowired
	public DoorServiceImpl(final DoorRepository doorRepository, final RoomService roomService,
						   final DoorConfigService doorConfigService) {
		this.doorRepository = doorRepository;
		this.roomService = roomService;
		this.doorConfigService = doorConfigService;
	}

	@Override
	public void deleteById(final Long id) {
		doorRepository.deleteById(id);
	}

	@Override
	public Door save(final Door door) {
		return doorRepository.save(door);
	}

	@Override
	public Optional<Door> findDoorById(Long id) {
		return doorRepository.findById(id);
	}

	@Override
	@Transactional
	public Door create(final Long roomId, final Door door) {

		final Door savedDoor = doorRepository.save(door);

		final Room room = roomService.getById(roomId);
		room.addDoor(savedDoor);

		roomService.save(room);

		doorConfigService.sendProofConfig(savedDoor.getName() + "_" + savedDoor.getId() + "_in",
				door.getProofConfigIn()
						.get(0));
		doorConfigService.sendProofConfig(savedDoor.getName() + "_" + savedDoor.getId() + "_out",
				door.getProofConfigOut()
						.get(0));
		return savedDoor;
	}
}
