package com.gpse.sesam.configuration;

import com.gpse.sesam.domain.credential.*;
import com.gpse.sesam.domain.location.*;
import com.gpse.sesam.domain.user.Issuer;
import com.gpse.sesam.domain.user.SesamUser;
import com.gpse.sesam.domain.user.SesamUserRole;
import com.gpse.sesam.domain.user.SesamUserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Profile("test")
public class InitializeDatabase implements InitializingBean {

    private final LocationService locationService;

    private final SesamUserService userService;

    private final CredentialService credentialService;

    private final CategoryService categoryService;

    private final PasswordEncoder passwordEncoder;

    List<Location> locationsList = new ArrayList<>();public InitializeDatabase(LocationService locationService, SesamUserService userService,
                              CredentialService credentialService, CategoryService categoryService,
                              PasswordEncoder passwordEncoder) {
        this.credentialService = credentialService;
        this.categoryService = categoryService;
        this.passwordEncoder = passwordEncoder;
        this.locationService = locationService;
        this.userService = userService;
    }

    @Override
    public void afterPropertiesSet() {
        locationService.deleteAll();
        categoryService.deleteAll();
        credentialService.deleteAll();
        userService.deleteAll();

        List<Location> locations = createLocations();
        List<SesamUser> users = createUsers();
        List<Credential> credentials = createCredentials();
        List<Category> categories = createCredentialCategories();

        locationService.saveAll(locations);
        userService.saveAll(users);
        credentialService.saveAll(credentials);
        categoryService.saveAll(categories);
    locationService.saveAll(locationsList);
	}

    private List<SesamUser> createUsers() {
        SesamUserRole adminRole = new SesamUserRole(SesamUserRole.AttainableRole.ADMINISTRATOR);
        adminRole.setGranted(true);
        SesamUserRole issuerRole = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
        issuerRole.setGranted(true);
        SesamUserRole editorRole = new SesamUserRole(SesamUserRole.AttainableRole.EDITOR);
        editorRole.setGranted(true);

        Door door = new Door("Door100", null);
        List<Door> doors = new ArrayList<>();Room room = new Room("0.007");
		Floor floor = new Floor(40, null);
		floor.addRoom(room);
		Building building = new Building("UHG");
		building.addFloor(floor);
		Location location = new Location("Heidelberg");
		location.addBuilding(building);

		room.addDoor(door);
        doors.add(door);

        String defaultPassword = passwordEncoder.encode("Hallo123!");
        SesamUser admin = new SesamUser("admin@test.de", defaultPassword, "Admin", "User",
                Collections.singletonList(adminRole));
        SesamUser issuer = new Issuer("issuer@test.de", defaultPassword, "Issuer", "User",
                Collections.singletonList(issuerRole), new Room("0.007"), null);
        SesamUser editor = new SesamUser("editor@test.de", defaultPassword, "Editor", "User",
                Collections.singletonList(editorRole));
        SesamUser user = new SesamUser("user@test.de", defaultPassword, "Test", "User",
                Collections.emptyList());


        return List.of(admin, issuer, editor, user);
    }

    private List<Location> createLocations() {
        List<Door> doors = new ArrayList<>();
        List<Door> doors2 = new ArrayList<>();

        for (int i = 0, k = 0; i < 60; i++, k += 2) {
            Door door = new Door("Door" + i, null);
            doors.add(door);
			Door door2 = new Door("Door" + k, null);
			doors2.add(door2);
        }

        List<Room> rooms = new ArrayList<>();
        List<Room> rooms2 = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Room room = new Room("Room " + i);
			room.addDoor(doors.get(i*2));
			room.addDoor(doors.get(i*2+1));
            rooms.add(room);
			Room room2 = new Room("Room " + i);
			room2.addDoor(doors2.get(i*2));
			room2.addDoor(doors2.get(i*2+1));
			rooms2.add(room2);
        }

        List<Floor> floors = new ArrayList<>();
        List<Floor> floors2 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Floor floor = new Floor(i % 2, "src/main/resources/citec-gebaeudeplan.png");
			floor.addRoom(rooms.get(i*5));
			floor.addRoom(rooms.get(i*5+1));
			floor.addRoom(
                    rooms.get(i*5+2));
			floor.addRoom(rooms.get(i*5+3));
			floor.addRoom(rooms.get(i*5+4));
            floors.add(floor);
			Floor floor2 = new Floor(i % 2, "src/main/resources/citec-gebaeudeplan.png");
			floor2.addRoom(
                    rooms2.get(i*5));
			floor2.addRoom(rooms2.get( i * 5 + 1));
			floor2.addRoom(rooms2.get(i*5+2));
			floor2.addRoom(rooms2.get(i*5+3));
			floor2.addRoom(rooms2.get(i*5+4));
			floors2.add(floor2);
        }

        List<Building> buildings = new ArrayList<>();
        List<Building> buildings2 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Building building = new Building("Building " + i);
			building.addFloor(floors.get(i*2));
			building.addFloor(floors.get(i*2+1));
            buildings.add(building);
			Building building2 = new Building("Building " + i);
			building2.addFloor(floors2.get(i*2));
			building2.addFloor(floors2.get(i*2+1));
			buildings2.add(building2);
		}
		Location location1 = new Location("Berlin");
		Location location2 = new Location("Bielefeld");

        for (int i=0; i < buildings.size(); i++) {
        location1.addBuilding(buildings.get(i));
        location2.addBuilding(buildings2.get(i));
		}

        return List.of(location1, location2);
    }

    private List<Credential> createCredentials() {
        // Checklist
        List<ChecklistEntry> checklist = new ArrayList<>();
        checklist.add(new ChecklistEntry("Wurde der Kurs erfolgreich abgeschlossen?"));
        checklist.add(new ChecklistEntry("Wurde der notwendige Nachweis erbracht?"));

        //Form
        List<FormEntry> form = new ArrayList<>();
        FormEntry id = new FormEntry("ID", FormEntryType.NUMBER, "id");
        FormEntry firstName = new FormEntry("Vorname", FormEntryType.TEXT, "first_name");
        FormEntry lastName = new FormEntry("Nachname", FormEntryType.TEXT, "last_name");
        FormEntry birthDate = new FormEntry("Geburtstagsdatum", FormEntryType.DATE, "birth_date");
        FormEntry date = new FormEntry("Ablaufdatum", FormEntryType.DATE, "expiration_date");
        form.add(id);
        form.add(firstName);
        form.add(lastName);
        form.add(birthDate);
        form.add(date);

        // Issuer
        SesamUserRole issuerRole10 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
        issuerRole10.setGranted(true);
        SesamUserRole issuerRole11 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
        issuerRole11.setGranted(true);


        Door door = new Door("Door999", null);
        List<Door> doors = new ArrayList<>();
        doors.add(door);

		Door door2 = new Door("Door666", null);
		List<Door> doors2 = new ArrayList<>();
		doors2.add(door2);

        Room room = new Room("0.007");
		room.addDoor(door);
        Room room2 = new Room("0.112");
		room2.addDoor(door2);
		Floor floor = new Floor(40, null);
		floor.addRoom(room);
		floor.addRoom(room2);
		Building building = new Building("UHG");
		building.addFloor(floor);
		Location location = new Location("Köln");
		location.addBuilding(building);
        List<Issuer> issuers = new ArrayList<>();
        Issuer issuer1 = new Issuer("peters@test.com", "Hallo123!", "Gerda", "Peters",
                Collections.singletonList(issuerRole10), room, Collections.singletonList(null));

        Issuer issuer2 = new Issuer("muster@test.com", "Hallo123!", "Erik", "Muster",
                Collections.singletonList(issuerRole11), room2, Collections.singletonList(null));

        issuers.add(issuer1);
        issuers.add(issuer2);

        // Safety-Credential
        List<Credential> credentials = new ArrayList<>();
        Credential safety = new Credential("Sicherheitsbelehrung-Uni", "$U-MEMBER",
                "university", form, checklist, issuers);

        List<ChecklistEntry> checklist3 = new ArrayList<>();
        checklist3.add(new ChecklistEntry("Wurde der Kurs erfolgreich abgeschlossen?"));
        checklist3.add(new ChecklistEntry("Wurde der notwendige Nachweis erbracht?"));

        List<FormEntry> form3 = new ArrayList<>();
        FormEntry id3 = new FormEntry("ID", FormEntryType.NUMBER, "id");
        FormEntry firstName3 = new FormEntry("Vorname", FormEntryType.TEXT, "first_name");
        FormEntry lastName3 = new FormEntry("Nachname", FormEntryType.TEXT, "last_name");
        FormEntry birthDate3 = new FormEntry("Geburtstagsdatum", FormEntryType.DATE, "birth_date");
        FormEntry date3 = new FormEntry("Ablaufdatum", FormEntryType.DATE, "expiration_date");
        form3.add(id3);
        form3.add(firstName3);
        form3.add(lastName3);
        form3.add(birthDate3);
        form3.add(date3);
        Credential safety2 = new Credential("Sicherheitsbelehrung-FH", "$T-MEMBER",
                "tlabs", form3, checklist3, issuers);
        credentials.add(safety);
        credentials.add(safety2);

        return credentials;
    }

    private List<Category> createCredentialCategories() {
        // Checklist
        List<ChecklistEntry> checklist4 = new ArrayList<>();
        checklist4.add(new ChecklistEntry("Wurde der Kurs erfolgreich abgeschlossen?"));
        checklist4.add(new ChecklistEntry("Wurde der notwendige Nachweis erbracht?"));

        //Form
        List<FormEntry> form4 = new ArrayList<>();
        FormEntry id4 = new FormEntry("ID", FormEntryType.NUMBER, "id");
        FormEntry firstName4 = new FormEntry("Vorname", FormEntryType.TEXT, "first_name");
        FormEntry lastName4 = new FormEntry("Nachname", FormEntryType.TEXT, "last_name");
        FormEntry birthDate4 = new FormEntry("Geburtstagsdatum", FormEntryType.DATE, "birth_date");
        FormEntry date4 = new FormEntry("Ablaufdatum", FormEntryType.DATE, "expiration_date");
        form4.add(id4);
        form4.add(firstName4);
        form4.add(lastName4);
        form4.add(birthDate4);
        form4.add(date4);

		// Issuer
		SesamUserRole issuerRole10 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole10.setGranted(true);
		SesamUserRole issuerRole11 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole10.setGranted(true);
		Room room = new Room("0.007");
		Room room2 = new Room("0.112");



		List<Issuer> issuers = new ArrayList<>();
		Issuer issuer1 = new Issuer("mann@test.com", "Hallo123!", "Elfriede", "Mann",
				Collections.singletonList(issuerRole10), room, Collections.singletonList(null));

        Issuer issuer2 = new Issuer("hombach@test.com", "Hallo123!", "Johann",
                "Hombach", Collections.singletonList(issuerRole11), room2, Collections.singletonList(null));
        issuers.add(issuer1);
        issuers.add(issuer2);

        // Safety-Credential
        List<Credential> credentials = new ArrayList<>();
        Credential safety = new Credential("Sicherheitsbelehrung-Baumschule", "$T-MEMBER",
                "tlabs", form4, checklist4, issuers);
        credentials.add(safety);
        List<ExternalCredential> externalCredentials = new ArrayList<>();
        ExternalCredential safety3 = new ExternalCredential("Sicherheitsbelehrung-Telekom",
                "$T-MEMBER");

        externalCredentials.add(safety3);


        //First-Aid-Credential
        List<Issuer> issuers2 = new ArrayList<>();
        issuers2.add(issuer1);
        // Checklist
        List<ChecklistEntry> checklist6 = new ArrayList<>();
        checklist6.add(new ChecklistEntry("Wurde der Kurs erfolgreich abgeschlossen?"));
        checklist6.add(new ChecklistEntry("Wurde der notwendige Nachweis erbracht?"));

        //Form
        List<FormEntry> form6 = new ArrayList<>();
        FormEntry id6 = new FormEntry("ID", FormEntryType.NUMBER, "id");
        FormEntry firstName6 = new FormEntry("Vorname", FormEntryType.TEXT, "first_name");
        FormEntry lastName6 = new FormEntry("Nachname", FormEntryType.TEXT, "last_name");
        FormEntry birthDate6 = new FormEntry("Geburtstagsdatum", FormEntryType.DATE, "birth_date");
        FormEntry date6 = new FormEntry("Ablaufdatum", FormEntryType.DATE, "expiration_date");
        form6.add(id6);
        form6.add(firstName6);
        form6.add(lastName6);
        form6.add(birthDate6);
        form6.add(date6);


        List<Credential> credentials2 = new ArrayList<>();
        Credential firstAid = new Credential("Erste-Hilfe-Kurs-DRK", "$U-TRAINING",
                "university", form6, checklist6, issuers);
        credentials2.add(firstAid);

        List<ExternalCredential> externalCredentials2 = new ArrayList<>();
        ExternalCredential firstAid2 = new ExternalCredential("Erste-Hilfe-Kurs-Telekom",
                "$U-TRAINING");
        ExternalCredential firstAid3 = new ExternalCredential("Erste-Hilfe-Kurs-Johanniter",
                "$U-MEMBER");

        externalCredentials2.add(firstAid2);
        externalCredentials2.add(firstAid3);

        //Rooms with Credentials

        Door door3 = new Door("Tor120", null);
		door3.addCredential(safety);

        List<Door> doors3 = new ArrayList<>();

        doors3.add(door3);

        Room room3 = new Room("120");
		room3.addDoor(door3);

        List<Room> roomList = new ArrayList<>();

        roomList.add(room3);

        Floor floor3 = new Floor(1, "src/main/resources/citec-gebaeudeplan.png");
		floor3.addRoom(room3);
		floor3.addRoom(room);
		floor3.addRoom(room2);

        List<Floor> floorList = new ArrayList<>();

        floorList.add(floor3);

        Building building3 = new Building("Citec");
		building3.addFloor(floor3);

        List<Building> buildingList = new ArrayList<>();

        buildingList.add(building3);

        Location location = new Location("Hamburg");
		location.addBuilding(building3);

        Door door4 = new Door("Tor1506", null);
		door4.addCredential(firstAid);

        List<Door> doors4 = new ArrayList<>();

        doors4.add(door4);

        Room room4 = new Room("0.150");
		room4.addDoor(door4);

        List<Room> roomList2 = new ArrayList<>();

        roomList2.add(room4);

        Floor floor4 = new Floor(1, "src/main/resources/citec-gebaeudeplan.png");
		floor4.addRoom(room4);

        List<Floor> floorList2 = new ArrayList<>();

        floorList2.add(floor4);

        Building building4 = new Building("Citec2");

        List<Building> buildingList2 = new ArrayList<>();building4.addFloor(floor4);

        buildingList2.add(building4);

        Location location2 = new Location("Bremen");
		location2.addBuilding(building4);
		locationsList.add(location);
		locationsList.add(location2);


        // Category
        List<Category> categories = new ArrayList<>();
        Category category =new Category("Sicherheitsbelehrung",  externalCredentials);
        category.addCredential(safety);
		Category category2 =new Category("Erste-Hilfe-Kurs", externalCredentials2);
		category2.addCredential(firstAid);
		categories.add(category);
		categories.add(category2);
        return categories;
    }
}