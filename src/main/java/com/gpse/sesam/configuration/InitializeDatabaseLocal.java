package com.gpse.sesam.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.domain.location.Building;
import com.gpse.sesam.domain.location.Coordinate;
import com.gpse.sesam.domain.location.Door;
import com.gpse.sesam.domain.location.Floor;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.domain.location.Room;
import com.gpse.sesam.domain.user.SesamUser;
import com.gpse.sesam.domain.user.SesamUserRole;
import com.gpse.sesam.domain.user.SesamUserService;
import com.gpse.sesam.util.GeoJsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Profile("test")
public class InitializeDatabaseLocal implements InitializingBean {

	private static final Logger LOG = LoggerFactory.getLogger(InitializeDatabaseLocal.class);
	private final LocationService locationService;

	private final SesamUserService userService;
	private final PasswordEncoder passwordEncoder;



	public InitializeDatabaseLocal(LocationService locationService, SesamUserService userService,
								   PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
		this.locationService = locationService;
		this.userService = userService;
	}

	@Override
	public void afterPropertiesSet() {
		locationService.deleteAll();
		userService.deleteAll();

		List<Location> locations = createLocations();
		List<SesamUser> users = createUsers();

		locationService.saveAll(locations);
		userService.saveAll(users);

	}

	private List<SesamUser> createUsers() {
		SesamUserRole adminRole = new SesamUserRole(SesamUserRole.AttainableRole.ADMINISTRATOR);
		adminRole.setGranted(true);
		SesamUserRole issuerRole = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole.setGranted(true);
		SesamUserRole editorRole = new SesamUserRole(SesamUserRole.AttainableRole.EDITOR);
		editorRole.setGranted(true);

		String defaultPassword = passwordEncoder.encode("Hallo123!");
		SesamUser admin = new SesamUser("admin@test.de", defaultPassword, "Admin", "User",
				Collections.singletonList(adminRole));
		SesamUser issuer = new SesamUser("issuer@test.de", defaultPassword, "Issuer", "User",
				Collections.singletonList(issuerRole));
		SesamUser editor = new SesamUser("editor@test.de", defaultPassword, "Editor", "User",
				Collections.singletonList(editorRole));
		SesamUser user = new SesamUser("user@test.de", defaultPassword, "Test", "User", Collections.emptyList());


		return List.of(admin, issuer, editor, user);
	}

	private List<Location> createLocations() {
		List<Door> doors = new ArrayList<>();
		List<Door> doors2 = new ArrayList<>();
		for (int i = 0, k = 0; i < 60; i++, k += 2) {
			doors.add(new Door("Door" + i));
			doors2.add(new Door("D00r" + k));
		}

		List<Room> rooms = new ArrayList<>();
		List<Room> rooms2 = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			rooms.add(new Room("Room " + i, doors.subList(i * 2, i * 2 + 2)));
			rooms2.add(new Room("Room " + i, doors2.subList(i * 2, i * 2 + 2)));
		}

		String jsonContent = readJsonFile();
		List<List<Coordinate>> roomCoordinates = createRoomCoordinates(jsonContent);

		for (int i = 0; i < roomCoordinates.size(); i++) {
			rooms.get(i).setCoordinates(roomCoordinates.get(i));
		}

		List<List<Coordinate>> doorCoordinates = createDoorCoordinates(jsonContent);

		for (int i = 0; i < doorCoordinates.size(); i++) {
			Door door = new Door("door" + i, doorCoordinates.get(i));
			rooms.get(i).setDoors(Collections.singletonList(door));
		}

		List<Floor> floors = new ArrayList<>();
		List<Floor> floors2 = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			floors.add(new Floor(i % 2, "src/main/resources/citec-gebaeudeplan.png", rooms.subList(i * 5, i * 5 + 5)));
			floors2.add(new Floor(i % 2, "src/main/resources/citec-gebaeudeplan.png", rooms2.subList(i * 5,
					i * 5 + 5)));
		}

		List<Building> buildings = new ArrayList<>();
		List<Building> buildings2 = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			buildings.add(new Building("Building " + i, floors.subList(i * 2, i * 2 + 2)));
			buildings2.add(new Building("Building " + i, floors2.subList(i * 2, i * 2 + 2)));

		}
		Location location1 = new Location("ExampleLocation", buildings);
		Location location2 = new Location("ExampleLocation2", buildings2);
		return List.of(location1, location2);
	}

	private String readJsonFile() {
		try {
			return String.join("", Files.readAllLines(Paths.get("src/main/resources/test_coordinates"
					+ ".json"), StandardCharsets.UTF_8));
		} catch (IOException e) {
			LOG.warn("Could not read json file", e);
		}
		return "";
	}

	private List<List<Coordinate>> createRoomCoordinates(String jsonContent) {
		try {
			return GeoJsonParser.parsePolygonsFromGeoJson(jsonContent);
		} catch (JsonProcessingException e) {
			LOG.warn("Coordination Data could not be initialized", e);
		}
		return Collections.emptyList();
	}

	private List<List<Coordinate>> createDoorCoordinates(String jsonContent) {
		try {
			return GeoJsonParser.parseLinesFromGeoJson(jsonContent);
		} catch (JsonProcessingException e) {
			LOG.warn("Coordination Data could not be initialized", e);
		}
		return Collections.emptyList();
	}
}
