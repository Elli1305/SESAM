package com.gpse.sesam.domain.location.room;

import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.floor.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Diese Klasse implementiert den RoomService und stellt Funktionen zur Verwaltung von Räumen bereit.
 */
@Service
public class RoomServiceImpl implements RoomService {

	private final RoomRepository roomRepository;
	private final FloorService floorService;

	/**
	 * Konstruktor für RoomServiceImpl.
	 *
	 * @param roomRepository das RoomRepository zur Datenbankabfrage von Räumen
	 * @param floorService   der FloorService zur Verwaltung von Etagen
	 */
	@Autowired
	public RoomServiceImpl(final RoomRepository roomRepository, final FloorService floorService) {
		this.roomRepository = roomRepository;
		this.floorService = floorService;
	}

	/**
	 * Löscht einen Raum anhand der angegebenen ID.
	 *
	 * @param id die ID des zu löschenden Raums
	 */
	@Override
	public void deleteById(final Long id) {
		roomRepository.deleteById(id);
	}

	/**
	 * Speichert einen Raum.
	 *
	 * @param room der zu speichernde Raum
	 * @return der gespeicherte Raum
	 */
	@Override
	public Room save(final Room room) {
		return roomRepository.save(room);
	}

	/**
	 * Gibt alle Räume zurück.
	 *
	 * @return eine Liste aller Räume
	 */
	@Override
	public List<Room> getRooms() {
		final List<Room> rooms = new ArrayList<>();
		roomRepository.findAll().forEach(rooms::add);
		return rooms;
	}


	/**
	 * Ruft einen Raum anhand der angegebenen ID ab.
	 *
	 * @param id die ID des abzurufenden Raums
	 * @return der gefundene Raum
	 * @throws IllegalArgumentException wenn kein Raum mit der angegebenen ID existiert
	 */
	@Override
	public Room getById(final Long id) {
		return roomRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("room with id " + id + " does not exist"));
	}

	/**
	 * Ruft einen Raum anhand der angegebenen ID ab.
	 *
	 * @param id die ID des abzurufenden Raums
	 * @return Optional, das den gefundenen Raum oder null enthält
	 */
	@Override
	public Optional<Room> getRoomById(final Long id) {
		return roomRepository.findById(id);
	}

	/**
	 * Gibt die Etage zurück, zu der der Raum mit der angegebenen Raum-ID gehört.
	 *
	 * @param roomId die ID des Raums
	 * @return die Etage, zu der der Raum gehört
	 */
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
