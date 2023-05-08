package com.gpse.sesam.configuration;

import com.gpse.sesam.domain.credential.*;
import com.gpse.sesam.domain.location.Building;
import com.gpse.sesam.domain.location.Floor;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.domain.location.Room;
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
		userService.deleteAll();
		credentialService.deleteAll();
		categoryService.deleteAll();

		List<Location> locations = createLocations();
		List<SesamUser> users = createUsers();
		List<Credential> credentials = createCredentials();
		List<Category> categories = createCredentialCategories();

		locationService.saveAll(locations);
		userService.saveAll(users);
		categoryService.saveAll(categories);
		credentialService.saveAll(credentials);
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
		SesamUser issuer = new Issuer("issuer@test.de", defaultPassword, "Issuer", "User",
				Collections.singletonList(issuerRole), new Room("0.007"), null);
		SesamUser editor = new SesamUser("editor@test.de", defaultPassword, "Editor", "User",
				Collections.singletonList(editorRole));
		SesamUser user = new SesamUser("user@test.de", defaultPassword, "Test", "User", Collections.emptyList());

		return List.of(admin, issuer, editor, user);
	}

	private List<Location> createLocations() {
		List<Room> rooms = new ArrayList<>();
		List<Room> rooms2 = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			rooms.add(new Room("Room " + i));
			rooms2.add(new Room("Room " + i));
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
		Room room = new Room("0.007");
		Room room2 = new Room("0.112");
		List<Issuer> issuers = new ArrayList<>();
		Issuer issuer1 = new Issuer("peters@test.com", "Hallo123!", "Gerda", "Peters", Collections.singletonList(issuerRole10),
				room, Collections.singletonList(null));

		Issuer issuer2 = new Issuer("muster@test.com", "Hallo123!", "Erik", "Muster", Collections.singletonList(issuerRole11),
				  room2, Collections.singletonList(null));

		issuers.add(issuer1);
		issuers.add(issuer2);

		// Safety-Credential
		List<Credential> credentials = new ArrayList<>();
		Credential safety = new Credential( "Sicherheitsbelehrung-Uni", "$U-Member", form, checklist, issuers);


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
		Credential safety2 = new Credential( "Sicherheitsbelehrung-FH", "$T-Member", form3, checklist3, issuers);
		credentials.add(safety);
		credentials.add(safety2);

		return credentials;
	}

	private List<Category> createCredentialCategories() {
		// Checklist
		List<ChecklistEntry> checklist = new ArrayList<>();
		checklist.add(new ChecklistEntry("Wurde der Kurs erfolgreich abgeschlossen?"));
		checklist.add(new ChecklistEntry( "Wurde der notwendige Nachweis erbracht?"));

		//Form
		List<FormEntry> form = new ArrayList<>();
		FormEntry id = new FormEntry( "ID", FormEntryType.NUMBER);
		FormEntry firstName = new FormEntry("Vorname", FormEntryType.TEXT);
		FormEntry lastName = new FormEntry( "Nachname", FormEntryType.TEXT);
		FormEntry birthDate = new FormEntry( "Geburtstagsdatum", FormEntryType.DATE);
		FormEntry date = new FormEntry("Ablaufdatum", FormEntryType.DATE);
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
		Room room = new Room("0.007");
		Room room2 = new Room("0.112");
		List<Issuer> issuers = new ArrayList<>();
		Issuer issuer1 = new Issuer("peters@test.com", "Hallo123!", "Gerda", "Peters", Collections.singletonList(issuerRole10),
				room, Collections.singletonList(null));

		Issuer issuer2 = new Issuer("muster@test.com", "Hallo123!", "Erik", "Muster", Collections.singletonList(issuerRole11),
				room2, Collections.singletonList(null));
		issuers.add(issuer1);
		issuers.add(issuer2);

		// Safety-Credential
		List<Credential> credentials = new ArrayList<>();
		Credential safety = new Credential( "Sicherheitsbelehrung-Uni", "$U-Member", form, checklist, issuers);
		Credential safety2 = new Credential( "Sicherheitsbelehrung-FH", "$T-Member", form, checklist, issuers);
		credentials.add(safety);
		credentials.add(safety2);
		List<ExternalCredential> externalCredentials = new ArrayList<>();
		ExternalCredential safety3 = new ExternalCredential( "Sicherheitsbelehrung-Telekom", "$T-Member");

		externalCredentials.add(safety3);


		//First-Aid-Credential
		List<Issuer> issuers2 = new ArrayList<>();
		issuers2.add(issuer1);

		List<Credential> credentials2 = new ArrayList<>();
		Credential firstAid = new Credential( "Erste-Hilfe-Kurs-DRK", "$U-Training", form, checklist, issuers2);
		credentials2.add(firstAid);

		List<ExternalCredential> externalCredentials2 = new ArrayList<>();
		ExternalCredential firstAid2 = new ExternalCredential( "Erste-Hilfe-Kurs-Telekom", "$U-Training");

		ExternalCredential firstAid3 = new ExternalCredential( "Erste-Hilfe-Kurs-Johanniter","$U-Member");

		externalCredentials2.add(firstAid2);
		externalCredentials2.add(firstAid3);

		// Category
		List<Category> categories = new ArrayList<>();
		categories.add(new Category( "Sicherheitsbelehrung", credentials,externalCredentials));
		categories.add(new Category( "Erste-Hilfe-Kurs", credentials2, externalCredentials2));


		return categories;
	}

}
