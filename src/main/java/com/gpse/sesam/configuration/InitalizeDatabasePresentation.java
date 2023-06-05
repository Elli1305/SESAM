package com.gpse.sesam.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.domain.colors.Colors;
import com.gpse.sesam.domain.colors.ColorsService;
import com.gpse.sesam.domain.credential.category.Category;
import com.gpse.sesam.domain.credential.category.CategoryService;
import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.domain.credential.credentials.CredentialService;
import com.gpse.sesam.domain.credential.credentials.ExternalCredential;
import com.gpse.sesam.domain.credential.issuing.ChecklistEntry;
import com.gpse.sesam.domain.credential.issuing.FormEntry;
import com.gpse.sesam.domain.credential.issuing.FormEntryType;
import com.gpse.sesam.domain.location.Coordinate;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.domain.location.RoomGroupService;
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
@Profile("presentation")
public class InitalizeDatabasePresentation implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(InitializeDatabaseLocal.class);

    private final LocationService locationService;
    private final RoomGroupService roomGroupService;

    private final SesamUserService userService;

    private final CredentialService credentialService;
    private final ColorsService colorsService;

    private final CategoryService categoryService;

    private final PasswordEncoder passwordEncoder;

    private final List<Location> locationsList = new ArrayList<>();
    private final List<Category> categoryList = new ArrayList<>();

    public InitalizeDatabasePresentation(final LocationService locationService, final SesamUserService userService,
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
    public void afterPropertiesSet() throws Exception {
        final List<Colors> colors = createColors();
        final List<Credential> credentials = createCredentials();
        final List<SesamUser> users = createUsers();

        colorsService.saveAll(colors);
        credentialService.saveAll(credentials);
        userService.saveAll(users);
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

    private List<SesamUser> createUsers() {
        final SesamUserRole adminRole = new SesamUserRole(SesamUserRole.AttainableRole.ADMINISTRATOR);
        adminRole.setGranted(true);
        final SesamUserRole editorRole = new SesamUserRole(SesamUserRole.AttainableRole.EDITOR);
        editorRole.setGranted(true);
        final String defaultPassword = passwordEncoder.encode("Hallo123!");
        final SesamUser admin = new SesamUser("admin@test.de", defaultPassword, "Admin", "User",
                Collections.singletonList(adminRole));
        final SesamUser editor = new SesamUser("editor@test.de", defaultPassword, "Editor", "User",
                Collections.singletonList(editorRole));

        return List.of(admin, editor);
    }


    private List<Credential> createCredentials() {
        // breaks jar build from mater

        final String jsonContent = readJsonFile();
		final List<List<Coordinate>> roomCoordinates = createRoomCoordinates(jsonContent);

        final List<List<Coordinate>> doorCoordinates = createDoorCoordinates(jsonContent);

        // Checklist
        final List<ChecklistEntry> checklist = checklist();

        //Form
        final List<FormEntry> form = form();

        // Issuer
        final SesamUserRole issuerRole1 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
        issuerRole1.setGranted(true);
        final SesamUserRole editorRole2 = new SesamUserRole(SesamUserRole.AttainableRole.EDITOR);
        editorRole2.setGranted(true);


        final Door door = new Door("Tür 1", doorCoordinates.get(0));
        final Door door2 = new Door("Tür 2", doorCoordinates.get(1));
        final Room room = new Room("0.007");
        room.addDoor(door);
        room.setCoordinates(roomCoordinates.get(0));
        final Room room2 = new Room("0.112");
        room2.setCoordinates(roomCoordinates.get(1));
        room2.addDoor(door2);
        final Floor floor = new Floor(0, "/citec-gebaeudeplan.png");
        final Floor floor2 = new Floor(1, "/citec-gebaeudeplan.png");
        floor.addRoom(room);
        floor.addRoom(room2);
        final Building building = new Building("CITEC");
        building.addFloor(floor);
        building.addFloor(floor2);
        final Location location = new Location("Bielefeld");
        location.addBuilding(building);
        final Location location2 = new Location("Berlin");


        final String defaultPassword = passwordEncoder.encode("Hallo123!");

        final List<SesamUser> issuers = new ArrayList<>();
        final Issuer issuer1 = new Issuer("jana@test.com", defaultPassword, "Jana", "Editor-Issuer",
                List.of(issuerRole1, editorRole2), room2);
        issuers.add(issuer1);


        final List<Credential> credentials = new ArrayList<>();
        final Credential safety = new Credential("U-MEMBER", "$U-MEMBER",
                "university", form, checklist);
        safety.addIssuer(issuer1);
        final List<ChecklistEntry> checklist3 = checklist();

        final List<FormEntry> form3 = form();  //Form
        final Credential safety2 = new Credential("T-MEMBER", "$T-MEMBER",
                "tlabs", form3, checklist3);
        safety2.addIssuer(issuer1);
        credentials.add(safety);
        credentials.add(safety2);

        List<FormEntry> form4 = formTraining();
        final List<ChecklistEntry> checklist4 = checklist();
        final Credential safety3 = new Credential("T-TRAINING", "$T-TRAINING", "tlabs", form4, checklist4);
        credentials.add(safety3);

        final List<ExternalCredential> externalCredentials2 = new ArrayList<>();
        final ExternalCredential external = new ExternalCredential("FH-MEMBER", "$U-MEMBER");
        final ExternalCredential external2 = new ExternalCredential("Erste-Hilfe-Kurs-Johanniter", "$T-TRAINING");

        externalCredentials2.add(external);
        externalCredentials2.add(external2);

        final Category category = new Category("Mitarbeiter");
        category.addExternalCredential(external);
        category.addCredential(safety);
        category.addCredential(safety2);
        final Category category2 = new Category("Training");
        category2.addCredential(safety3);
        category2.addExternalCredential(external2);

        locationsList.add(location);
        locationsList.add(location2);
        locationService.saveAll(locationsList);
        categoryList.add(category2);
        categoryList.add(category);
        categoryService.saveAll(categoryList);
        credentialService.saveAll(credentials);
        userService.saveAll(issuers);

        return credentials;
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

    public List<FormEntry> formTraining() {
        final List<FormEntry> form = new ArrayList<>();
        final FormEntry firstName = new FormEntry("Vorname", FormEntryType.TEXT, "first_name");
        final FormEntry lastName = new FormEntry("Nachname", FormEntryType.TEXT, "last_name");
        final FormEntry date = new FormEntry("Ablaufdatum", FormEntryType.DATE, "expiration_date");
        final FormEntry trainingType = new FormEntry("Trainingstyp", FormEntryType.TEXT, "type_of_training");
        form.add(firstName);
        form.add(lastName);
        form.add(date);
        form.add(trainingType);
        return form;
    }

    private List<ChecklistEntry> checklist() {
        final List<ChecklistEntry> checklist = new ArrayList<>();
        checklist.add(new ChecklistEntry("Wurde der Kurs erfolgreich abgeschlossen?"));
        checklist.add(new ChecklistEntry("Wurde der notwendige Nachweis erbracht?"));
        return checklist;
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
