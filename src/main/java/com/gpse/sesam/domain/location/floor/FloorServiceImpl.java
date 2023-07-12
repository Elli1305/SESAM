package com.gpse.sesam.domain.location.floor;

import com.gpse.sesam.domain.filestorage.FileStorageService;
import com.gpse.sesam.domain.location.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse implementiert den FloorService und stellt Funktionen zur Verwaltung von Etagen bereit.
 */
@Service
public class FloorServiceImpl implements FloorService {

	private final FloorRepository floorRepository;

	private final FileStorageService fileStorageService;

	private final RoomRepository roomRepository;


	/**
	 * Konstruktor für FloorServiceImpl.
	 *
	 * @param floorRepository      das FloorRepository zur Datenbankabfrage von Etagen
	 * @param fileStorageService   der FileStorageService zur Verwaltung von Dateispeicherung
	 * @param roomRepository       das RoomRepository zur Datenbankabfrage von Räumen
	 */
	@Autowired
	public FloorServiceImpl(final FloorRepository floorRepository,
							final FileStorageService fileStorageService,
							RoomRepository roomRepository) {
		this.floorRepository = floorRepository;
		this.fileStorageService = fileStorageService;
		this.roomRepository = roomRepository;
	}

	/**
	 * Löscht eine Etage anhand der angegebenen ID.
	 *
	 * @param id die ID der zu löschenden Etage
	 */
	@Override
	public void deleteById(final Long id) {
		floorRepository.deleteById(id);
	}

	/**
	 * Speichert eine Etage mit der angegebenen Datei.
	 *
	 * @param floor         die zu speichernde Etage
	 * @param multipartFile die hochgeladene Datei
	 * @return die gespeicherte Etage
	 */
	@Override
	public Floor save(final Floor floor, final MultipartFile multipartFile) {
		final String filePath = fileStorageService.storeFile(multipartFile);
		floor.setFloorPlanPath(filePath);
		return floorRepository.save(floor);
	}

	/**
	 * Speichert eine Etage ohne Datei.
	 *
	 * @param floor die zu speichernde Etage
	 * @return die gespeicherte Etage
	 */
	@Override
	public Floor save(final Floor floor) {
		return floorRepository.save(floor);
	}

	/**
	 * Gibt alle Etagen zurück.
	 *
	 * @return eine Liste aller Etagen
	 */
	@Override
	public List<Floor> getAll() {
		final List<Floor> floors = new ArrayList<>();
		floorRepository.findAll().forEach(floors::add);
		return floors;
	}
}
