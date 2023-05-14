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

	List<Location> locationsList = new ArrayList<>();

	public InitializeDatabase(LocationService locationService, SesamUserService userService,
							  CredentialService credentialService, CategoryService categoryService, PasswordEncoder passwordEncoder) {
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
		List<Credential> noCredentials = new ArrayList<>();
		noCredentials.add(null);
		Door door = new Door("Door100", null ,noCredentials);
		List<Door> doors = new ArrayList<>();
		doors.add(door);

		String defaultPassword = passwordEncoder.encode("Hallo123!");
		SesamUser admin = new SesamUser("admin@test.de", defaultPassword, "Admin", "User",
				Collections.singletonList(adminRole));
		SesamUser issuer = new Issuer("issuer@test.de", defaultPassword, "Issuer", "User",
				Collections.singletonList(issuerRole), new Room("0.007", doors), null);
		SesamUser editor = new SesamUser("editor@test.de", defaultPassword, "Editor", "User",
				Collections.singletonList(editorRole));
		SesamUser user = new SesamUser("user@test.de", defaultPassword, "Test", "User", Collections.emptyList());


		return List.of(admin, issuer, editor, user);
	}

	private List<Location> createLocations() {
		List<Door> doors = new ArrayList<>();
		List<Door> doors2 = new ArrayList<>();
		List<Credential> credentials = new ArrayList<>();
		credentials.add(null);
		for(int i = 0, k = 0; i < 60; i++, k += 2) {
			doors.add(new Door("Door" + i, null, credentials));
			doors2.add(new Door("Door" + k, null, null));
		}

		List<Room> rooms = new ArrayList<>();
		List<Room> rooms2 = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			rooms.add(new Room("Room " + i, doors.subList(i * 2, i * 2 + 2)));
			rooms2.add(new Room("Room " + i, doors2.subList(i * 2, i * 2 + 2)));
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
		Location location1 = new Location("Berlin", buildings);
		Location location2 = new Location("Bielefeld", buildings2);
		return List.of(location1, location2);
	}

	private List<Credential> createCredentials() {
		// Checklist
		List<ChecklistEntry> checklist = new ArrayList<>();
		checklist.add(new ChecklistEntry( "Wurde der Kurs erfolgreich abgeschlossen?"));
		checklist.add(new ChecklistEntry("Wurde der notwendige Nachweis erbracht?"));

		//Form
		List<FormEntry> form = new ArrayList<>();
		FormEntry id = new FormEntry("ID", FormEntryType.NUMBER);
		FormEntry firstName = new FormEntry( "Vorname", FormEntryType.TEXT);
		FormEntry lastName = new FormEntry( "Nachname", FormEntryType.TEXT);
		FormEntry birthDate = new FormEntry("Geburtstagsdatum", FormEntryType.DATE);
		FormEntry date = new FormEntry( "Ablaufdatum", FormEntryType.DATE);
		form.add(id);
		form.add(firstName);
		form.add(lastName);
		form.add(birthDate);
		form.add(date);

		// Issuer
		SesamUserRole issuerRole10 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole10.setGranted(true);
		SesamUserRole issuerRole11 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole10.setGranted(true);

		List<Credential> noCredentials = new ArrayList<>();
		noCredentials.add(null);
		Door door = new Door("Door999", null, noCredentials);
		List<Door> doors = new ArrayList<>();
		doors.add(door);

		List<Credential> noCredentials2 = new ArrayList<>();
		noCredentials2.add(null);
		Door door2 = new Door("Door666", null, noCredentials);
		List<Door> doors2 = new ArrayList<>();
		doors2.add(door2);

		Room room = new Room("0.007", doors);
		door.setRoom(room);
		Room room2 = new Room("0.112", doors2);
		List<Issuer> issuers = new ArrayList<>();
		Issuer issuer1 = new Issuer("peters@test.com", "Hallo123!", "Gerda", "Peters",
				Collections.singletonList(issuerRole10), room, Collections.singletonList(null));

		Issuer issuer2 = new Issuer("muster@test.com", "Hallo123!", "Erik", "Muster",
				Collections.singletonList(issuerRole11), room2, Collections.singletonList(null));

		issuers.add(issuer1);
		issuers.add(issuer2);

		// Safety-Credential
		List<Credential> credentials = new ArrayList<>();
		Credential safety = new Credential( "Sicherheitsbelehrung-Uni", "$U-Member", null, form, checklist, issuers);


		List<ChecklistEntry> checklist3 = new ArrayList<>();
		checklist3.add(new ChecklistEntry( "Wurde der Kurs erfolgreich abgeschlossen?"));
		checklist3.add(new ChecklistEntry("Wurde der notwendige Nachweis erbracht?"));

		List<FormEntry> form3 = new ArrayList<>();
		FormEntry id3 = new FormEntry("ID", FormEntryType.NUMBER);
		FormEntry firstName3 = new FormEntry( "Vorname", FormEntryType.TEXT);
		FormEntry lastName3 = new FormEntry( "Nachname", FormEntryType.TEXT);
		FormEntry birthDate3 = new FormEntry("Geburtstagsdatum", FormEntryType.DATE);
		FormEntry date3 = new FormEntry( "Ablaufdatum", FormEntryType.DATE);
		form.add(id3);
		form.add(firstName3);
		form.add(lastName3);
		form.add(birthDate3);
		form.add(date3);
		Credential safety2 = new Credential( "Sicherheitsbelehrung-FH", "$T-Member", null, form3, checklist3, issuers);
		credentials.add(safety);
		credentials.add(safety2);

		return credentials;
	}

	private List<Category> createCredentialCategories() {
		// Checklist
		List<ChecklistEntry> checklist4 = new ArrayList<>();
		checklist4.add(new ChecklistEntry("Wurde der Kurs erfolgreich abgeschlossen?"));
		checklist4.add(new ChecklistEntry( "Wurde der notwendige Nachweis erbracht?"));

		//Form
		List<FormEntry> form4 = new ArrayList<>();
		FormEntry id4 = new FormEntry( "ID", FormEntryType.NUMBER);
		FormEntry firstName4 = new FormEntry("Vorname", FormEntryType.TEXT);
		FormEntry lastName4 = new FormEntry( "Nachname", FormEntryType.TEXT);
		FormEntry birthDate4 = new FormEntry( "Geburtstagsdatum", FormEntryType.DATE);
		FormEntry date4 = new FormEntry("Ablaufdatum", FormEntryType.DATE);
		form4.add(id4);
		form4.add(firstName4);
		form4.add(lastName4);
		form4.add(birthDate4);
		form4.add(date4);

		// Issuer
		List<Credential> noCredentials = new ArrayList<>();
		noCredentials.add(null);
		Door door = new Door("Tor", null, noCredentials);
		List<Door> doors = new ArrayList<>();
		doors.add(door);
		SesamUserRole issuerRole10 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole10.setGranted(true);
		SesamUserRole issuerRole11 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole10.setGranted(true);
		Room room = new Room("0.007", doors);


		List<Credential> noCredentials2 = new ArrayList<>();
		noCredentials2.add(null);
		Door door2 = new Door("Tor", null, noCredentials);
		List<Door> doors2 = new ArrayList<>();
		doors2.add(door2);
		Room room2 = new Room("0.112", doors2);



		List<Issuer> issuers = new ArrayList<>();
		Issuer issuer1 = new Issuer("mann@test.com", "Hallo123!", "Elfriede", "Mann",
				Collections.singletonList(issuerRole10), room, Collections.singletonList(null));

		Issuer issuer2 = new Issuer("hombach@test.com", "Hallo123!", "Johann", "Hombach",
				Collections.singletonList(issuerRole11), room2, Collections.singletonList(null));
		issuers.add(issuer1);
		issuers.add(issuer2);

		// Safety-Credential
		List<Credential> credentials = new ArrayList<>();
		Credential safety = new Credential( "Sicherheitsbelehrung-Baumschule", "$U-Member", null, form4, checklist4, issuers);
		credentials.add(safety);
		List<ExternalCredential> externalCredentials = new ArrayList<>();
		ExternalCredential safety3 = new ExternalCredential( "Sicherheitsbelehrung-Telekom", "$T-Member");

		externalCredentials.add(safety3);


		//First-Aid-Credential
		List<Issuer> issuers2 = new ArrayList<>();
		issuers2.add(issuer1);
		// Checklist
		List<ChecklistEntry> checklist6 = new ArrayList<>();
		checklist6.add(new ChecklistEntry("Wurde der Kurs erfolgreich abgeschlossen?"));
		checklist6.add(new ChecklistEntry( "Wurde der notwendige Nachweis erbracht?"));

		//Form
		List<FormEntry> form6 = new ArrayList<>();
		FormEntry id6 = new FormEntry( "ID", FormEntryType.NUMBER);
		FormEntry firstName6 = new FormEntry("Vorname", FormEntryType.TEXT);
		FormEntry lastName6 = new FormEntry( "Nachname", FormEntryType.TEXT);
		FormEntry birthDate6 = new FormEntry( "Geburtstagsdatum", FormEntryType.DATE);
		FormEntry date6 = new FormEntry("Ablaufdatum", FormEntryType.DATE);
		form6.add(id6);
		form6.add(firstName6);
		form6.add(lastName6);
		form6.add(birthDate6);
		form6.add(date6);


		List<Credential> credentials2 = new ArrayList<>();
		Credential firstAid = new Credential( "Erste-Hilfe-Kurs-DRK", "$U-Training", null, form6, checklist6, issuers2);
		credentials2.add(firstAid);

		List<ExternalCredential> externalCredentials2 = new ArrayList<>();
		ExternalCredential firstAid2 = new ExternalCredential( "Erste-Hilfe-Kurs-Telekom", "$U-Training");
		ExternalCredential firstAid3 = new ExternalCredential( "Erste-Hilfe-Kurs-Johanniter","$U-Member");

		externalCredentials2.add(firstAid2);
		externalCredentials2.add(firstAid3);

		//Rooms with Credentials
		Door door3 = new Door("Tor120", null, credentials2);
		List<Door> doors3 = new ArrayList<>();
		doors3.add(door3);
		Room room3 = new Room("120", doors3);
		List<Room> roomList = new ArrayList<>();
		roomList.add(room3);
		Floor floor3 = new Floor(1, "src/main/resources/citec-gebaeudeplan.png", roomList);
		List<Floor> floorList = new ArrayList<>();
		floorList.add(floor3);
		Building building3 = new Building("Citec",floorList );
		List<Building> buildingList = new ArrayList<>();
		buildingList.add(building3);
		Location location = new Location("Hamburg", buildingList);

		Door door4 = new Door("Tor1506", null, credentials);
		List<Door> doors4 = new ArrayList<>();
		doors3.add(door4);
		Room room4 = new Room("0.150", doors4);
		List<Room> roomList2 = new ArrayList<>();
		roomList2.add(room4);
		Floor floor4 = new Floor(1, "src/main/resources/citec-gebaeudeplan.png", roomList2);
		List<Floor> floorList2 = new ArrayList<>();
		floorList2.add(floor4);
		Building building4 = new Building("Citec2",floorList2);
		List<Building> buildingList2 = new ArrayList<>();
		buildingList2.add(building4);
		Location location2 = new Location("Bremen", buildingList2);
		locationsList.add(location);
		locationsList.add(location2);

		// Category
		List<Category> categories = new ArrayList<>();
		categories.add(new Category( "Sicherheitsbelehrung", credentials,externalCredentials));
		categories.add(new Category( "Erste-Hilfe-Kurs", credentials2, externalCredentials2));
		return categories;
	}
}