package com.gpse.sesam.domain.location.door;

import com.gpse.sesam.domain.location.door.config.DoorConfigService;
import com.gpse.sesam.domain.location.door.config.TwoWayDoorConfig;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomRepository;
import com.gpse.sesam.domain.location.room.RoomService;
import com.gpse.sesam.util.ActiveConfigUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * Diese Klasse implementiert den DoorService und stellt Funktionen zur Verwaltung von Türen bereit.
 */
@Service
public class DoorServiceImpl implements DoorService {

	private final DoorRepository doorRepository;

	private final RoomService roomService;

	private final RoomRepository roomRepository;

	private final DoorConfigService doorConfigService;

	/**
	 * Konstruktor für DoorServiceImpl.
	 *
	 * @param doorRepository    das DoorRepository zur Datenbankabfrage von Türen
	 * @param roomService       der RoomService zur Verwaltung von Räumen
	 * @param roomRepository    das RoomRepository zur Datenbankabfrage von Räumen
	 * @param doorConfigService der DoorConfigService zur Verwaltung der Türkonfiguration
	 */
	@Autowired
	public DoorServiceImpl(final DoorRepository doorRepository, final RoomService roomService,
						   RoomRepository roomRepository, final DoorConfigService doorConfigService) {
		this.doorRepository = doorRepository;
		this.roomService = roomService;
		this.roomRepository = roomRepository;
		this.doorConfigService = doorConfigService;
	}

	/**
	 * Löscht eine Tür anhand der angegebenen ID.
	 *
	 * @param id die ID der zu löschenden Tür
	 */
	@Override
	public void deleteById(final Long id) {
		doorRepository.deleteById(id);
	}

	/**
	 * Gibt alle Türen zurück
	 *
	 * @return die Liste aller Türen
	 */
	@Override
	public List<Door> getDoors() {
		final List<Door> doors = new ArrayList<>();

		doorRepository.findAll().forEach(doors::add);

		return doors;
	}

	/**
	 * Erstellt eine neue Tür.
	 *
	 * @param door Tür zum Erstellen
	 */
	@Override
	public Door save(final Door door) {
		Door savedDoor = doorRepository.save(door);

		scheduleActiveConfig(door, savedDoor);
		return savedDoor;
	}

	/**
	 * Prüft welche Config gerade gescheduled ist
	 * @param door für das Scheduling
	 * @param savedDoor Tür in der DB
	 */
	private void scheduleActiveConfig(Door door, Door savedDoor) {
		TwoWayDoorConfig activeConfig = ActiveConfigUtil.getCurrentConfig(door.getDoorConfigs());
		if (activeConfig != null) {
			doorConfigService.sendProofConfig(savedDoor.getName() + "_" + savedDoor.getId() + "_in",
					activeConfig.getProofConfigIn());
			doorConfigService.sendProofConfig(savedDoor.getName() + "_" + savedDoor.getId() + "_out",
					activeConfig.getProofConfigOut());
		}
	}

	/**
	 * Sucht eine Tür anhand der angegebenen ID.
	 *
	 * @param id die ID der gesuchten Tür
	 * @return Optional, das die gefundene Tür oder null enthält
	 */
	@Override
	public Optional<Door> findDoorById(Long id) {
		return doorRepository.findById(id);
	}

	/**
	 * Erstellt eine neue Tür im angegebenen Raum.
	 *
	 * @param roomId die ID des Raums, in dem die Tür erstellt werden soll
	 * @param door   die zu erstellende Tür
	 * @return die erstellte Tür
	 */
	@Override
	@Transactional
	public Door create(final Long roomId, final Door door) {

		final Door savedDoor = doorRepository.save(door);

		final Room room = roomService.getById(roomId);
		room.addDoor(savedDoor);

		roomService.save(room);
		scheduleActiveConfig(door, savedDoor);
		return savedDoor;
	}

	/**
	 * Gibt eine Liste von Türen zurück, die zum angegebenen Raum gehören.
	 *
	 * @param id die ID des Raums
	 * @return eine Liste von Türen, die zum Raum gehören, oder eine leere Liste, wenn kein Raum gefunden wurde
	 */
	@Override
	public List<Door> getDoorsByRoomId(Long id) {
		Optional<Room> room = roomRepository.findById(id);
		if (room.isPresent()) {
			return room.get().getDoors();
		}
		return Collections.emptyList();
	}
}
