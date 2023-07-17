package com.gpse.sesam.domain.location.building;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Diese Klasse implementiert den BuildingService und stellt Funktionen zur Verwaltung von Gebäuden bereit.
 */
@Service
public class BuildingServiceImpl implements BuildingService {

	private final BuildingRepository buildingRepository;

	/**
	 * Konstruktor für BuildingServiceImpl.
	 *
	 * @param buildingRepository das BuildingRepository zur Datenbankabfrage von Gebäuden
	 */

	@Autowired
	public BuildingServiceImpl(final BuildingRepository buildingRepository) {
		this.buildingRepository = buildingRepository;
	}

	/**
	 * Löscht ein Gebäude anhand der angegebenen ID.
	 *
	 * @param id die ID des zu löschenden Gebäudes
	 */
	@Override
	public void deleteById(final Long id) {
		buildingRepository.deleteById(id);
	}

	/**
	 * Speichert ein Gebäude.
	 *
	 * @param building das zu speichernde Gebäude
	 * @return das gespeicherte Gebäude
	 */
	@Override
	public Building save(final Building building) {
		return buildingRepository.save(building);
	}
}
