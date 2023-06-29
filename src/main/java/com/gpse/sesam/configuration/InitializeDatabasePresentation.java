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
import com.gpse.sesam.domain.location.door.config.*;
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
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
@Profile("presentation")
public class InitializeDatabasePresentation implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(InitializeDatabaseLocal.class);

    private final LocationService locationService;
    private final RoomGroupService roomGroupService;

    private final SesamUserService userService;

    private final CredentialService credentialService;
    private final ColorsService colorsService;

    private final DoorConfigService doorConfigService;

    private final CategoryService categoryService;

    private final PasswordEncoder passwordEncoder;

    private final List<Location> locationsList = new ArrayList<>();
    private final List<Category> categoryList = new ArrayList<>();

    private ProofConfig proofConfig;

    public InitializeDatabasePresentation(final LocationService locationService, final SesamUserService userService,
                                         final CredentialService credentialService, final ColorsService colorsService,
                                         final CategoryService categoryService, final PasswordEncoder passwordEncoder,
                                         final RoomGroupService roomGroupService, DoorConfigService doorConfigService) {
        this.credentialService = credentialService;
        this.colorsService = colorsService;
        this.categoryService = categoryService;
        this.passwordEncoder = passwordEncoder;
        this.locationService = locationService;
        this.userService = userService;
        this.roomGroupService = roomGroupService;
        this.doorConfigService = doorConfigService;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        proofConfig = createProofConfig();
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

        //Tür 1
        Coordinate coordinate = new Coordinate(new BigDecimal(-83.62), new BigDecimal(-12.49));
        Coordinate coordinate1 = new Coordinate(new BigDecimal(-77), new BigDecimal(-12.49));

        //Tür 2
        Coordinate coordinate2 = new Coordinate(new BigDecimal(-45.76), new BigDecimal(-12.49));
        Coordinate coordinate3 = new Coordinate(new BigDecimal(-39.64), new BigDecimal(-12.49));

        //Room 0.007
        Coordinate coordinate4 = new Coordinate(new BigDecimal(-86.62), new BigDecimal(-90));
        Coordinate coordinate5 = new Coordinate(new BigDecimal(-86.62), new BigDecimal(-12.49));

        Coordinate coordinate6 = new Coordinate(new BigDecimal(-37.10), new BigDecimal(-12.49));
        Coordinate coordinate7 = new Coordinate(new BigDecimal(-37.10), new BigDecimal(-90));

        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(coordinate);
        coordinates.add(coordinate1);
        List<Coordinate> coordinates1 = new ArrayList<>();
        coordinates1.add(coordinate2);
        coordinates1.add(coordinate3);
        List<Coordinate> coordinates2 = new ArrayList<>();
        coordinates2.add(coordinate4);
        coordinates2.add(coordinate5);
        coordinates2.add(coordinate6);
        coordinates2.add(coordinate7);

        final Door door = new Door("Door1", coordinates);
        final Door door2 = new Door("Door2", coordinates1);
        final Room room = new Room("0.007");
        room.addDoor(door);
        room.addDoor(door2);
        room.setCoordinates(coordinates2);
        final Door door3 = new Door("Door3", doorCoordinates.get(0));
        final Room room2 = new Room("0.112");
        room2.setCoordinates(roomCoordinates.get(0));
        room2.addDoor(door3);
        final Floor floor = new Floor(0, "/citec-gebaeudeplan.png");
        final Floor floor2 = new Floor(1, "/citec-gebaeudeplan.png");
        floor.addRoom(room);
        floor.addRoom(room2);
        final Building building = new Building("CITEC");
        building.addFloor(floor);
        building.addFloor(floor2);
        final Location location = new Location("Bielefeld");
        location.addBuilding(building);


        final String defaultPassword = passwordEncoder.encode("Hallo123!");

        final List<SesamUser> issuers = new ArrayList<>();
        final Issuer issuer1 = new Issuer("jana@test.com", defaultPassword, "Jana", "Editor-Issuer",
                List.of(issuerRole1, editorRole2), room2);
        issuers.add(issuer1);


        final List<Credential> credentials = new ArrayList<>();
        final Credential safety = new Credential("U-MEMBER", "$U-MEMBER",
                "university", form, checklist);
        door.addCredential(safety);
        safety.addIssuer(issuer1);
        issuer1.addCredential(safety);
        final List<ChecklistEntry> checklist3 = checklist();

        final List<FormEntry> form3 = form();  //Form
        final Credential safety2 = new Credential("T-MEMBER", "$T-MEMBER",
                "tlabs", form3, checklist3);
        safety2.addIssuer(issuer1);
        issuer1.addCredential(safety2);
        credentials.add(safety);
        credentials.add(safety2);

        List<FormEntry> form4 = formTraining();
        final List<ChecklistEntry> checklist4 = checklist();
        final Credential safety3 = new Credential("T-TRAINING", "$T-TRAINING", "tlabs", form4, checklist4);
        credentials.add(safety3);
        issuer1.addCredential(safety3);

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
        locationService.saveAll(locationsList);

        doorConfigService.sendProofConfig( "Door1_" + door.getId(), proofConfig );
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

    private ProofConfig createProofConfig() {
        final ProofConfig proofConfig = new ProofConfig();
        proofConfig.setDescription("Bitte präsentieren Sie ein U-Member-Credential.");

        final Map<String, ProofPredicateInfo> requestedPredicates = new HashMap<>();
        final ProofPredicateInfo predicateInfo = new ProofPredicateInfo();
        predicateInfo.setName("expiration_date");
        predicateInfo.setPredicateType(">");
        predicateInfo.setPredicateValue("$TODAY-YYYYMMDD");
        final List<AttributeFilter> predicateRestrictions = new ArrayList<>();
        predicateRestrictions.add(new AttributeFilter());
        predicateRestrictions.get(0).setCredentialDefinitionId("$U-MEMBER");
        predicateInfo.setRestrictions(predicateRestrictions);
        requestedPredicates.put("expiration_date", predicateInfo);
        proofConfig.setRequestedPredicates(requestedPredicates);

        return proofConfig;
    }
}
