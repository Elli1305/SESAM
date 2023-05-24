package com.gpse.sesam.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.domain.location.*;
import com.gpse.sesam.domain.user.SesamUser;
import com.gpse.sesam.domain.user.SesamUserRole;
import com.gpse.sesam.domain.user.SesamUserService;
import com.gpse.sesam.util.GeoJsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Profile("test")
public class InitializeDatabaseLocal implements InitializingBean {

	private static final Logger LOG = LoggerFactory.getLogger(InitializeDatabaseLocal.class);
	private final LocationService locationService;
	private final RoomGroupService roomGroupService;

	private final SesamUserService userService;
	private final PasswordEncoder passwordEncoder;

	private File floorplanDir = new File("data" + File.separator + "floorplan");

	public InitializeDatabaseLocal(final LocationService locationService, final SesamUserService userService,
								   final PasswordEncoder passwordEncoder, final RoomGroupService roomGroupService) {
		this.passwordEncoder = passwordEncoder;
		this.locationService = locationService;
		this.userService = userService;
		this.roomGroupService = roomGroupService;

		if (!floorplanDir.exists() && !floorplanDir.mkdir()) {
			throw new BeanCreationException("Folder: " + floorplanDir + " could not be created.");
		}
	}

	@Override
	public void afterPropertiesSet() {
		locationService.deleteAll();
		userService.deleteAll();
		roomGroupService.deleteAll();

		final List<Location> locations = createLocations();
		final List<SesamUser> users = createUsers();
		final List<RoomGroups> roomGroups = roomGroups(locations);

		locationService.saveAll(locations);
		userService.saveAll(users);
		roomGroupService.saveAll(roomGroups);

	}

	private List<SesamUser> createUsers() {
		final SesamUserRole adminRole = new SesamUserRole(SesamUserRole.AttainableRole.ADMINISTRATOR);
		adminRole.setGranted(true);
		final SesamUserRole issuerRole = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole.setGranted(true);
		final SesamUserRole editorRole = new SesamUserRole(SesamUserRole.AttainableRole.EDITOR);
		editorRole.setGranted(true);

		final String defaultPassword = passwordEncoder.encode("Hallo123!");
		final String lastName = "User";
		final SesamUser admin = new SesamUser("admin@test.de", defaultPassword, "Admin", lastName,
				Collections.singletonList(adminRole));
		final SesamUser issuer = new SesamUser("issuer@test.de", defaultPassword, "Issuer", lastName,
				Collections.singletonList(issuerRole));
		final SesamUser editor = new SesamUser("editor@test.de", defaultPassword, "Editor", lastName,
				Collections.singletonList(editorRole));
		final SesamUser user = new SesamUser("user@test.de", defaultPassword, "Test", lastName,
				Collections.emptyList());

		LOG.info("Testdata successfully inserted");
		return List.of(admin, issuer, editor, user);
	}

	private List<RoomGroups> roomGroups(List<Location> locations) {
		final List<RoomGroups> roomGroups = new ArrayList<>();
		final Building build = locations.get(0).getBuildings().get(0);
		final Building build2 = locations.get(0).getBuildings().get(1);
		final List<Room> rooms = build.getFloors().get(0).getRooms();

		final List<Room> rooms2 = build2.getFloors().get(0).getRooms();
		roomGroups.add(new RoomGroups("Frozen Jaghurt", rooms, build));
		roomGroups.add(new RoomGroups("Group 2", rooms2, build2));
		return roomGroups;
	}

	private List<Location> createLocations() {
		final List<Door> doors = new ArrayList<>();
		final List<Door> doors2 = new ArrayList<>();
		for (int i = 0; i < 60; i++) {
			doors.add(new Door("Door" + i));
			doors2.add(new Door("D00r" + i));
		}

		final List<Room> rooms = new ArrayList<>();
		final List<Room> rooms2 = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			rooms.add(new Room("Room " + i, doors.subList(i * 2, i * 2 + 2)));
			rooms2.add(new Room("Room " + i, doors2.subList(i * 2, i * 2 + 2)));
		}

		final String jsonContent = readJsonFile();
		final List<List<Coordinate>> roomCoordinates = createRoomCoordinates(jsonContent);

		for (int i = 0; i < roomCoordinates.size(); i++) {
			rooms.get(i).setCoordinates(roomCoordinates.get(i));
		}

		final List<List<Coordinate>> doorCoordinates = createDoorCoordinates(jsonContent);

		for (int i = 0; i < doorCoordinates.size(); i++) {
			final Door door = new Door("door" + i, doorCoordinates.get(i));
			rooms.get(i).setDoors(Collections.singletonList(door));
		}

		final List<Floor> floors = new ArrayList<>();
		final List<Floor> floors2 = new ArrayList<>();
		final String floorPlanPath = "/citec-gebaeudeplan.png";
		for (int i = 0; i < 6; i++) {
			floors.add(new Floor(i % 2, floorPlanPath, rooms.subList(i * 5, i * 5 + 5)));
			floors2.add(new Floor(i % 2, floorPlanPath, rooms2.subList(i * 5,
					i * 5 + 5)));
		}

		final List<Building> buildings = new ArrayList<>();
		final List<Building> buildings2 = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			buildings.add(new Building("Building " + i, floors.subList(i * 2, i * 2 + 2)));
			buildings2.add(new Building("Building " + i, floors2.subList(i * 2, i * 2 + 2)));

		}
		final Location location1 = new Location("ExampleLocation", buildings);
		final Location location2 = new Location("ExampleLocation2", buildings2);

		return List.of(location1, location2);
	}

	private String readJsonFile() {
		try (InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("test_coordinates.json")) {
			return new String(is.readAllBytes(), StandardCharsets.UTF_8);
		} catch (final IOException e) {
			LOG.warn("Could not read json file", e);
		}
		return "";
	}

	private List<List<Coordinate>> createRoomCoordinates(final String jsonContent) {
		try {
			return GeoJsonParser.parsePolygonsFromGeoJson(jsonContent);
		} catch (final JsonProcessingException e) {
			LOG.warn("Coordination Data could not be initialized", e);
		}
		return Collections.emptyList();
	}

	private List<List<Coordinate>> createDoorCoordinates(final String jsonContent) {
		try {
			return GeoJsonParser.parseLinesFromGeoJson(jsonContent);
		} catch (final JsonProcessingException e) {
			LOG.warn("Coordination Data could not be initialized", e);
		}
		return Collections.emptyList();
	}
}
