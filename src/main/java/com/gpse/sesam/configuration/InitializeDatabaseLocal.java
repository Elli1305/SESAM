package com.gpse.sesam.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.domain.colors.Colors;
import com.gpse.sesam.domain.colors.ColorsService;
import com.gpse.sesam.domain.credential.category.Category;
import com.gpse.sesam.domain.credential.category.CategoryService;
import com.gpse.sesam.domain.credential.credentials.InternalCredential;
import com.gpse.sesam.domain.credential.credentials.CredentialService;
import com.gpse.sesam.domain.credential.credentials.ExternalCredential;
import com.gpse.sesam.domain.credential.issuing.ChecklistEntry;
import com.gpse.sesam.domain.credential.issuing.FormEntry;
import com.gpse.sesam.domain.credential.issuing.FormEntryType;
import com.gpse.sesam.domain.location.Coordinate;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.domain.location.roomgroup.RoomGroupService;
import com.gpse.sesam.domain.location.roomgroup.RoomGroups;
import com.gpse.sesam.domain.location.building.Building;
import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.user.SesamUser;
import com.gpse.sesam.domain.user.SesamUserRole;
import com.gpse.sesam.domain.user.SesamUserService;
import com.gpse.sesam.domain.user.issuer.Issuer;
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
	private final RoomGroupService roomGroupService;

	private final SesamUserService userService;

	private final CredentialService credentialService;
	private final ColorsService colorsService;

	private final CategoryService categoryService;

	private final PasswordEncoder passwordEncoder;

	private final List<Location> locationsList = new ArrayList<>();

	public InitializeDatabaseLocal(final LocationService locationService, final SesamUserService userService,
								   final CredentialService credentialService, final ColorsService colorsService,
								   final CategoryService categoryService, final PasswordEncoder passwordEncoder,
								   final RoomGroupService roomGroupService) {
		this.credentialService = credentialService;
		this.colorsService = colorsService;
		this.categoryService = categoryService;
		this.passwordEncoder = passwordEncoder;
		this.locationService = locationService;
		this.userService = userService;
		this.roomGroupService = roomGroupService;
	}

	@Override
	public void afterPropertiesSet() {
		final List<Location> locations = createLocations();
		final List<InternalCredential> credentials = createCredentials();
		final List<SesamUser> users = createUsers(credentials);
		final List<Category> categories = createCredentialCategories();
		final List<Colors> colors = createColors();
		final List<RoomGroups> roomGroups = roomGroups(locations);

		colorsService.saveAll(colors);
		locationService.saveAll(locations);
		credentialService.saveAll(credentials);
		locationService.saveAll(locationsList);
		categoryService.saveAll(categories);
		userService.saveAll(users);
		roomGroupService.saveAll(roomGroups);
	}

	private List<Colors> createColors() {
		final Colors defaultColors = new Colors();
		defaultColors.setDefaultColors(true);
		setColors(defaultColors);

		final Colors currentColors = new Colors();
		currentColors.setDefaultColors(false);
		setColors(currentColors);

		final List<Colors> colors = new ArrayList<>();
		colors.add(defaultColors);
		colors.add(currentColors);

		return colors;
	}

	private void setColors(final Colors defaultColors) {
		defaultColors.setBgC("#ffffff");
		defaultColors.setTextC("#000000");
		defaultColors.setPrimaryColor("#e20074");
		defaultColors.setSecondary("#f6b2d5");
		defaultColors.setAccent("#ffffff");
		defaultColors.setDark("#808080");
		defaultColors.setLightBlue("#7d99a7");
		defaultColors.setPositive("#dcdcdc");
		defaultColors.setNegative("#505050");
		defaultColors.setInfo("#0074E2");
		defaultColors.setWarning("#fec705");
	}

	private List<SesamUser> createUsers(final List<InternalCredential> credentials) {
		final SesamUserRole adminRole = new SesamUserRole(SesamUserRole.AttainableRole.ADMINISTRATOR);
		adminRole.setGranted(true);
		final SesamUserRole issuerRole = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole.setGranted(true);
		final SesamUserRole editorRole = new SesamUserRole(SesamUserRole.AttainableRole.EDITOR);
		editorRole.setGranted(true);
		final String defaultPassword = passwordEncoder.encode("Hallo123!");
		final SesamUser admin = new SesamUser("admin@test.de", defaultPassword, "Admin", "User",
				List.of(adminRole));
		final Issuer issuer = new Issuer("issuer@test.de", defaultPassword, "Issuer", "User",
				List.of(issuerRole), new Room("0.007"));
		issuer.setCredentials(credentials);
		final SesamUser editor = new SesamUser("editor@test.de", defaultPassword, "Editor", "User",
				List.of(editorRole));
		final SesamUser user = new SesamUser("user@test.de", defaultPassword, "Test", "User",
				Collections.emptyList());

		for (final InternalCredential cred : credentials) {
			cred.getIssuer().add(issuer);
		}

		final SesamUserRole issuerRole20 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole20.setGranted(true);
		final SesamUserRole editorRole21 = new SesamUserRole(SesamUserRole.AttainableRole.EDITOR);
		editorRole21.setGranted(true);

		final SesamUser jana = new SesamUser("jana@test.de", defaultPassword, "Jana", "Editor-Issuer",
				List.of(editorRole21, issuerRole20));


		return List.of(admin, issuer, editor, user, jana);
	}

	private List<RoomGroups> roomGroups(final List<Location> locations) {
		final List<RoomGroups> roomGroups = new ArrayList<>();
		final Building build = locations.get(0).getBuildings().get(0);
		final Building build2 = locations.get(0).getBuildings().get(1);
		final List<Room> rooms = build.getFloors().get(0).getRooms();

		final List<Room> rooms2 = build2.getFloors().get(1).getRooms();
		roomGroups.add(new RoomGroups("Frozen Jaghurt", rooms, build));
		roomGroups.add(new RoomGroups("Group 2", rooms2, build2));
		return roomGroups;
	}

	private List<Location> createLocations() {
		final List<Door> doors = new ArrayList<>();
		final List<Door> doors2 = new ArrayList<>();

		for (int i = 0, k = 0; i < 60; i++, k += 2) {
			final Door door = new Door("Door" + i, null);
			doors.add(door);
			final Door door2 = new Door("Door" + k, null);
			doors2.add(door2);
		}

		final List<Room> rooms = new ArrayList<>();
		final List<Room> rooms2 = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			final Room room = new Room("Room " + i);
			room.addDoor(doors.get(i * 2));
			room.addDoor(doors.get(i * 2 + 1));
			rooms.add(room);
			final Room room2 = new Room("Room " + i);
			room2.addDoor(doors2.get(i * 2));
			room2.addDoor(doors2.get(i * 2 + 1));
			rooms2.add(room2);
		}

		// breaks jar build from mater
		final String jsonContent = readJsonFile();
		final List<List<Coordinate>> roomCoordinates = createRoomCoordinates(jsonContent);

		for (int i = 0; i < roomCoordinates.size(); i++) {
			rooms.get(i).setCoordinates(roomCoordinates.get(i));
		}

		final List<List<Coordinate>> doorCoordinates = createDoorCoordinates(jsonContent);

		for (int i = 0; i < doorCoordinates.size(); i++) {
			final Door door = new Door("door" + i, doorCoordinates.get(i));
			rooms.get(i).setDoors(List.of(door));
		}

		final List<Floor> floors = new ArrayList<>();
		final List<Floor> floors2 = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			final Floor floor = new Floor(i % 2, "/citec-gebaeudeplan.png");
			final Floor floor2 = new Floor(i % 2, "/citec-gebaeudeplan.png");
			for (int j = 0; j < 5; j++) {
				floor.addRoom(rooms.get(i * 5 + j));
				floor2.addRoom(rooms2.get(i * 5 + j));
			}
			floors.add(floor);
			floors2.add(floor2);
		}

		final List<Building> buildings = new ArrayList<>();
		final List<Building> buildings2 = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			final Building building = new Building("Building " + i);
			building.addFloor(floors.get(i * 2));
			building.addFloor(floors.get(i * 2 + 1));
			buildings.add(building);
			final Building building2 = new Building("Building " + i);
			building2.addFloor(floors2.get(i * 2));
			building2.addFloor(floors2.get(i * 2 + 1));
			buildings2.add(building2);
		}
		final Location location1 = new Location("Berlin");
		final Location location2 = new Location("Bielefeld");

		for (int i = 0; i < buildings.size(); i++) {
			location1.addBuilding(buildings.get(i));
			location2.addBuilding(buildings2.get(i));
		}

		return List.of(location1, location2);
	}

	private List<FormEntry> form() {
		final List<FormEntry> form = new ArrayList<>();
		final FormEntry id = new FormEntry("ID", FormEntryType.NUMBER, "id");
		final FormEntry firstName = new FormEntry("Vorname", FormEntryType.TEXT, "first_name");
		final FormEntry lastName = new FormEntry("Nachname", FormEntryType.TEXT, "last_name");
		final FormEntry birthDate = new FormEntry("Geburtstagsdatum", FormEntryType.DATE, "birth_date");
		final FormEntry date = new FormEntry("Ablaufdatum", FormEntryType.DATE, "expiration_date");
		form.add(id);
		form.add(firstName);
		form.add(lastName);
		form.add(birthDate);
		form.add(date);
		return form;
	}

	private List<ChecklistEntry> checklist() {
		final List<ChecklistEntry> checklist = new ArrayList<>();
		checklist.add(new ChecklistEntry("Wurde der Kurs erfolgreich abgeschlossen?"));
		checklist.add(new ChecklistEntry("Wurde der notwendige Nachweis erbracht?"));
		return checklist;
	}

	private List<InternalCredential> createCredentials() {
		// Checklist
		final List<ChecklistEntry> checklist = checklist();

		//Form
		final List<FormEntry> form = form();

		// Issuer
		final SesamUserRole issuerRole10 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole10.setGranted(true);
		final SesamUserRole issuerRole11 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole11.setGranted(true);


		final Door door = new Door("Door999", null);
		final Door door2 = new Door("Door666", null);

		final Room room = new Room("0.007");
		room.addDoor(door);
		final Room room2 = new Room("0.112");
		room2.addDoor(door2);
		final Floor floor = new Floor(40, null);
		floor.addRoom(room);
		floor.addRoom(room2);
		final Building building = new Building("UHG");
		building.addFloor(floor);
		final Location location = new Location("Köln");
		location.addBuilding(building);
		final String defaultPassword = passwordEncoder.encode("Hallo123!");
		final List<Issuer> issuers = new ArrayList<>();
		final Issuer issuer1 = new Issuer("peters@test.com", defaultPassword, "Gerda", "Peters",
				List.of(issuerRole10), room);

		final Issuer issuer2 = new Issuer("muster@test.com", defaultPassword, "Erik", "Muster",
				List.of(issuerRole11), room2);

		issuers.add(issuer1);
		issuers.add(issuer2);

		final List<ChecklistEntry> checklist3 = checklist();

		final List<FormEntry> form3 = form();  //Form
		final InternalCredential safety2 = new InternalCredential("Sicherheitsbelehrung-FH", "$T-TRAINING",
				"tlabs", form3, checklist3);
		safety2.addIssuer(issuer1);
		safety2.addIssuer(issuer2);


		return List.of(safety2);
	}

	private List<Category> createCredentialCategories() {
		// Checklist
		final List<ChecklistEntry> checklist4 = checklist();

		//Form
		final List<FormEntry> form4 = form();

		// Issuer
		final SesamUserRole issuerRole10 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole10.setGranted(true);
		final SesamUserRole issuerRole11 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole10.setGranted(true);
		final Room room = new Room("0.007");
		final Room room2 = new Room("0.112");


		final List<Issuer> issuers = new ArrayList<>();
		final Issuer issuer1 = new Issuer("mann@test.com", "Hallo123!", "Elfriede", "Mann",
				List.of(issuerRole10), room);

		final Issuer issuer2 = new Issuer("hombach@test.com", "Hallo123!", "Johann",
				"Hombach", List.of(issuerRole11), room2);
		issuers.add(issuer1);
		issuers.add(issuer2);


		// Safety-Credential
		final List<InternalCredential> credentials = new ArrayList<>();
		final InternalCredential safety = new InternalCredential(
				"Sicherheitsbelehrung-Baumschule",
				"$T-MEMBER",
				"tlabs",
				form4,
				checklist4
		);
		safety.addIssuer(issuer1);
		safety.addIssuer(issuer2);
		credentials.add(safety);
		final List<FormEntry> form7 = form();
		final List<ExternalCredential> externalCredentials = new ArrayList<>();
		final ExternalCredential safety3 = new ExternalCredential("Sicherheitsbelehrung-Telekom", "$T-TRAINING", form7);

		externalCredentials.add(safety3);


		//First-Aid-Credential
		final List<Issuer> issuers2 = new ArrayList<>();
		issuers2.add(issuer1);
		// Checklist
		final List<ChecklistEntry> checklist6 = checklist();

		//Form
		final List<FormEntry> form6 = form();

		final List<InternalCredential> credentials2 = new ArrayList<>();
		final InternalCredential firstAid = new InternalCredential(
				"Erste-Hilfe-Kurs-DRK",
				"$U-TRAINING",
				"university",
				form6,
				checklist6
		);
		firstAid.addIssuer(issuer2);
		credentials2.add(firstAid);

		final List<FormEntry> form8 = form();
		final List<FormEntry> form9 = form();
		final List<ExternalCredential> externalCredentials2 = new ArrayList<>();
		final ExternalCredential firstAid2 = new ExternalCredential("Erste-Hilfe-Kurs-Telekom", "$U-TRAINING", form8);
		final ExternalCredential firstAid3 = new ExternalCredential("Erste-Hilfe-Kurs-Johanniter", "$U-MEMBER", form9);

		externalCredentials2.add(firstAid2);
		externalCredentials2.add(firstAid3);

		//Rooms with Credentials

		final Door door3 = new Door("Tor120", null);

		final List<Door> doors3 = new ArrayList<>();

		doors3.add(door3);

		final Room room3 = new Room("120");
		room3.addDoor(door3);

		final List<Room> roomList = new ArrayList<>();
		roomList.add(room3);

		final Floor floor3 = new Floor(1, "/citec-gebaeudeplan.png");
		floor3.addRoom(room3);
		floor3.addRoom(room);
		floor3.addRoom(room2);

		final List<Floor> floorList = new ArrayList<>();

		floorList.add(floor3);

		final Building building3 = new Building("Citec");
		building3.addFloor(floor3);

		final List<Building> buildingList = new ArrayList<>();

		buildingList.add(building3);

		final Location location = new Location("Hamburg");
		location.addBuilding(building3);

		final Door door4 = new Door("Tor1506", null);

		final List<Door> doors4 = new ArrayList<>();

		doors4.add(door4);

		final Room room4 = new Room("0.150");
		room4.addDoor(door4);

		final List<Room> roomList2 = new ArrayList<>();

		roomList2.add(room4);

		final Floor floor4 = new Floor(1, "/citec-gebaeudeplan.png");
		floor4.addRoom(room4);

		final List<Floor> floorList2 = new ArrayList<>();

		floorList2.add(floor4);

		final Building building4 = new Building("Citec2");

		final List<Building> buildingList2 = new ArrayList<>();
		building4.addFloor(floor4);

		buildingList2.add(building4);

		final Location location2 = new Location("Bremen");
		location2.addBuilding(building4);
		locationsList.add(location);
		locationsList.add(location2);

		// Category

		final List<Category> categories = new ArrayList<>();
		final Category category = new Category("Sicherheitsbelehrung");
		category.addExternalCredential(safety3);
		category.addCredential(safety);
		final Category category2 = new Category("Erste-Hilfe-Kurs");
		category2.addCredential(firstAid);
		category2.addExternalCredential(firstAid2);
		category2.addExternalCredential(firstAid3);
		categories.add(category);
		categories.add(category2);

		return categories;
	}


	private String readJsonFile() {
		try {
			return String.join("", Files.readAllLines(Paths.get("src/main/resources/test_coordinates"
					+ ".json"), StandardCharsets.UTF_8));
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