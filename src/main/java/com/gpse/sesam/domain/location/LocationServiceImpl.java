package com.gpse.sesam.domain.location;

import com.gpse.sesam.web.exception.LocationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Diese Klasse implementiert den LocationService und stellt Funktionen zur Verwaltung von Standorten bereit.
 */
@Service
public class LocationServiceImpl implements LocationService {

	private final LocationRepository locationRepository;

	/**
	 * Konstruktor für LocationServiceImpl.
	 *
	 * @param locationRepository das LocationRepository zur Datenbankabfrage von Standorten
	 */
	@Autowired
	public LocationServiceImpl(final LocationRepository locationRepository) {
		this.locationRepository = locationRepository;
	}

	/**
	 * Gibt eine Liste aller Standorte zurück.
	 *
	 * @return eine Liste aller Standorte
	 */
	@Override
	public List<Location> getLocations() {
		final List<Location> locations = new ArrayList<>();

		locationRepository.findAll().forEach(locations::add);

		return locations;
	}

	/**
	 * Ruft einen Standort anhand der angegebenen ID ab.
	 *
	 * @param id die ID des abzurufenden Standorts
	 * @return Optional, das den gefundenen Standort oder null enthält
	 */
	@Override
	public Optional<Location> getLocation(final Long id) {
		return locationRepository.findById(id);
	}

	/**
	 * Ruft einen Standort anhand des angegebenen Namens ab.
	 *
	 * @param name der Name des abzurufenden Standorts
	 * @return Optional, das den gefundenen Standort oder null enthält
	 * @throws LocationNotFoundException wenn kein Standort mit dem angegebenen Namen gefunden wurde
	 */
	@Override
	public Optional<Location> getLocationByName(final String name) throws LocationNotFoundException {
		return Optional.ofNullable(locationRepository.findByName(name)
				.orElseThrow(() -> new LocationNotFoundException(name + "Not found")));
	}

	/**
	 * Speichert eine Liste von Standorten.
	 *
	 * @param locations die zu speichernden Standorte
	 */
	@Override
	public void saveAll(final Iterable<Location> locations) {
		locationRepository.saveAll(locations);
	}


	/**
	 * Speichert einen Standort.
	 *
	 * @param location der zu speichernde Standort
	 * @return der gespeicherte Standort
	 */
	@Override
	public Location save(Location location) {
		return locationRepository.save(location);
	}

	/**
	 * Löscht einen Standort anhand der angegebenen ID.
	 *
	 * @param id die ID des zu löschenden Standorts
	 */
	@Override
	public void deleteById(Long id) {
		locationRepository.deleteById(id);
	}
}
