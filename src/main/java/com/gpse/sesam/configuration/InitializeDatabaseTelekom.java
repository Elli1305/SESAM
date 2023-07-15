package com.gpse.sesam.configuration;

import com.gpse.sesam.domain.colors.ColorTheme;
import com.gpse.sesam.domain.colors.Colors;
import com.gpse.sesam.domain.colors.ColorsService;
import com.gpse.sesam.domain.credential.category.Category;
import com.gpse.sesam.domain.credential.category.CategoryService;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredential;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredentialService;
import com.gpse.sesam.domain.credential.credentials.internal.CredentialService;
import com.gpse.sesam.domain.credential.credentials.internal.InternalCredential;
import com.gpse.sesam.domain.credential.issue.issuing.ChecklistEntry;
import com.gpse.sesam.domain.credential.issue.issuing.FormEntry;
import com.gpse.sesam.domain.credential.issue.issuing.FormEntryType;
import com.gpse.sesam.domain.filestorage.FileStorageService;
import com.gpse.sesam.domain.location.Coordinate;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.domain.location.building.Building;
import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.DoorService;
import com.gpse.sesam.domain.location.door.config.*;
import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.user.SesamUser;
import com.gpse.sesam.domain.user.SesamUserRole;
import com.gpse.sesam.domain.user.SesamUserService;
import com.gpse.sesam.domain.user.issuer.Issuer;
import com.gpse.sesam.web.exception.FileStorageException;
import jakarta.validation.OverridesAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Profile("telekom")
public class InitializeDatabaseTelekom implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(InitializeDatabaseLocal.class);

    private final LocationService locationService;

    private final SesamUserService userService;

    private final CredentialService credentialService;
    private final ColorsService colorsService;

    private final FileStorageService fileStorageService;

    private final DoorService doorService;

    private final PasswordEncoder passwordEncoder;

    private final ExternalCredentialService externalCredentialService;
    private final CategoryService categoryService;

    public InitializeDatabaseTelekom(final LocationService locationService, final SesamUserService userService,
                                     final CredentialService credentialService, final ColorsService colorsService,
                                     FileStorageService fileStorageService, final PasswordEncoder passwordEncoder,
                                     DoorService doorService, ExternalCredentialService externalCredentialService, CategoryService categoryService) {
        this.credentialService = credentialService;
        this.colorsService = colorsService;
        this.fileStorageService = fileStorageService;
        this.passwordEncoder = passwordEncoder;
        this.locationService = locationService;
        this.userService = userService;
        this.doorService = doorService;
        this.externalCredentialService = externalCredentialService;
        this.categoryService = categoryService;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        final List<Colors> colors = createColors();
        final List<InternalCredential> credentials = createCredentials();
        final List<SesamUser> users = createUsers(credentials);
        final List<Location> locations = createLocations();
        final List<ExternalCredential> externalCredentials = createExternalCredential();

        final List<Category> categories = createCategories(credentials, externalCredentials);


        colorsService.saveAll(colors);
        setLogo();
        credentialService.saveAll(credentials);
        userService.saveAll(users);
        locationService.saveAll(locations);
        doorService.save(locations.get(0).getBuildings().get(0).getFloors().get(0).getRooms().get(0).getDoors().get(0));
        externalCredentialService.saveAll(externalCredentials);
        categoryService.saveAll(categories);
    }

    private List<ExternalCredential> createExternalCredential() {
        List<ExternalCredential> externalCredentials = new ArrayList<>();

        ExternalCredential utraining= new ExternalCredential("U-Training", "1.0", "$U-TRAINING", formTraining());

        externalCredentials.add(utraining);

        return externalCredentials;
    }

    private List<Category> createCategories(List<InternalCredential> internal, List<ExternalCredential> external) {
        List<Category> categories = new ArrayList<>();

        Category category = new Category("hazard-handling");
        category.addCredential(internal.get(1));
        internal.get(1).setCategory(category);

        for (ExternalCredential externalCredential: external) {
            externalCredential.setCategory(category);
            category.addExternalCredential(externalCredential);
        }

        categories.add(category);

        return categories;
    }


    private List<Colors> createColors() {
        final Colors defaultLight = new Colors();
        defaultLight.setDefaultColors(true);
        defaultLight.setTheme(ColorTheme.LIGHT);
        setLightColors(defaultLight);
        defaultLight.setLogoPath("T_logo_white.svg");

        final Colors defaultDark = new Colors();
        defaultDark.setDefaultColors(true);
        defaultDark.setTheme(ColorTheme.DARK);
        setDarkColors(defaultDark);
        defaultDark.setLogoPath("T_logo_black.svg");

        final Colors currentLight = new Colors();
        currentLight.setDefaultColors(false);
        currentLight.setTheme(ColorTheme.LIGHT);
        setLightColors(currentLight);

        final Colors currentDark = new Colors();
        currentDark.setDefaultColors(false);
        currentDark.setTheme(ColorTheme.DARK);
        setDarkColors(currentDark);

        final List<Colors> colors = new ArrayList<>();
        colors.add(defaultLight);
        colors.add(defaultDark);
        colors.add(currentLight);
        colors.add(currentDark);

        return colors;
    }

    private void setLightColors(final Colors defaultColors) {
        defaultColors.setLogoPath("/Logo.svg");
        defaultColors.setBgC("#ffffff");
        defaultColors.setTextC("#000000");
        defaultColors.setPrimaryColor("#e20074");
        defaultColors.setSecondary("#f6b2d5");
        defaultColors.setAccent("#ffffff");
        defaultColors.setDark("#808080");
        defaultColors.setLight("#9e9e9e");
        defaultColors.setPositive("#dcdcdc");
        defaultColors.setNegative("#505050");
        defaultColors.setInfo("#0074E2");
        defaultColors.setWarning("#fec705");
    }

    private void setDarkColors(final Colors defaultColors) {
        defaultColors.setLogoPath("/Logo-Dark.svg");
        defaultColors.setBgC("#303030");
        defaultColors.setTextC("#ffffff");
        defaultColors.setPrimaryColor("#e20074");
        defaultColors.setSecondary("#f6b2d5");
        defaultColors.setAccent("#000000");
        defaultColors.setDark("#808080");
        defaultColors.setLight("#9e9e9e");
        defaultColors.setPositive("#dcdcdc");
        defaultColors.setNegative("#505050");
        defaultColors.setInfo("#0074E2");
        defaultColors.setWarning("#fec705");
    }

    private void setLogo() {
        try {
            fileStorageService.reset(ColorTheme.LIGHT);
            fileStorageService.reset(ColorTheme.DARK);
        } catch (FileStorageException ignored) {
            LOG.warn("Unable to reset fileStorageService.");
        }
    }

    private List<SesamUser> createUsers(List<InternalCredential> internals) {
        //User Roles
        final SesamUserRole adminRole = new SesamUserRole(SesamUserRole.AttainableRole.ADMINISTRATOR);
        adminRole.setGranted(true);
        final SesamUserRole editorRole = new SesamUserRole(SesamUserRole.AttainableRole.EDITOR);
        editorRole.setGranted(true);
        final SesamUserRole issuerRole = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
        issuerRole.setGranted(true);

        //Users
        final String defaultPassword = passwordEncoder.encode("Hallo123!");

        final SesamUser admin = new SesamUser("admin@test.de", defaultPassword, "T-Labs", "Admin",
                Collections.singletonList(adminRole));
        final SesamUser editor = new SesamUser("editor@test.de", defaultPassword, "T-Labs", "Editor",
                Collections.singletonList(editorRole));
        final Issuer issuer = new Issuer("wunderland@sesam.de", defaultPassword, "Gerhard", "Wunderland",
                Collections.singletonList(issuerRole), new Room("106"));
        issuer.setCredentials(List.of(internals.get(1)));
        internals.get(1).setIssuer(List.of(issuer));

        return List.of(admin, editor, issuer);
    }


    private List<InternalCredential> createCredentials() {

        List<InternalCredential> internalCredentials = new ArrayList<>();

        internalCredentials.add(new InternalCredential(
                "T-Member", "1.0", "$T-MEMBER",
                "tlabs", formMember(), checklistMember()));
        internalCredentials.add(new InternalCredential(
                "T-Training", "1.0", "$T-TRAINING",
                "tlabs", formTraining(), checklistTraining()));

        return internalCredentials;
    }

    private List<Location> createLocations() {

        //Room 0.114
        List<Coordinate> coordinates = new ArrayList<>();

        coordinates.add(new Coordinate(new BigDecimal("21.18"), new BigDecimal("68.85")));
        coordinates.add(new Coordinate(new BigDecimal("54.99"), new BigDecimal("68.85")));
        coordinates.add(new Coordinate(new BigDecimal("54.99"), new BigDecimal("45.56")));
        coordinates.add(new Coordinate(new BigDecimal("12.18"), new BigDecimal("45.56")));
        coordinates.add(new Coordinate(new BigDecimal("12.18"), new BigDecimal("57.95")));
        coordinates.add(new Coordinate(new BigDecimal("21.18"), new BigDecimal("57.95")));

        final Room room = new Room("Forschungslabor 0.114");
        room.setCoordinates(coordinates);

        //Door to Room 0.114
        List<Coordinate> doorCoordinates = new ArrayList<>();
        ProofConfig proofConfig = createProofConfig();
        ProofConfig proofConfig2 = createProofConfig();

        TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
        twoWayDoorConfig.setBaseConfig(true);
        twoWayDoorConfig.setProofConfigIn(proofConfig);
        twoWayDoorConfig.setProofConfigOut(proofConfig2);

        doorCoordinates.add(new Coordinate(new BigDecimal("23.81"), new BigDecimal("68.85")));
        doorCoordinates.add(new Coordinate(new BigDecimal("26.81"), new BigDecimal("68.85")));

        Door door = new Door("Eingang-1", doorCoordinates);
        door.setDoorConfigs(List.of(twoWayDoorConfig));

        room.addDoor(door);

        //Floor
        final Floor floor = new Floor(0, "/citec-gebaeudeplan.svg");
        floor.addRoom(room);

        //Building
        final Building building = new Building("CITEC");
        building.addFloor(floor);

        //Location
        List<Location> locations = new ArrayList<>();

        final Location location = new Location("Bielefeld");
        location.addBuilding(building);

        locations.add(location);

        return locations;
    }

    private List<FormEntry> formMember() {
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
        final FormEntry trainingType = new FormEntry("Trainingstyp", FormEntryType.TEXT, "type");
        form.add(firstName);
        form.add(lastName);
        form.add(date);
        form.add(trainingType);
        return form;
    }

    private List<ChecklistEntry> checklistMember() {
        final List<ChecklistEntry> checklist = new ArrayList<>();
        checklist.add(new ChecklistEntry("Wurde der notwendige Nachweis erbracht?"));
        return checklist;
    }

    private List<ChecklistEntry> checklistTraining() {
        final List<ChecklistEntry> checklist = new ArrayList<>();
        checklist.add(new ChecklistEntry("Wurde ein Gefahrentraining absolviert und liegt ein Nachweis vor?"));
        return checklist;
    }

    private ProofConfig createProofConfig() {
        final ProofConfig proofConfig = new ProofConfig();
        proofConfig.setDescription("Bitte präsentieren Sie ein U-Member oder T-Member Credential.");

        final Map<String, ProofPredicateInfo> requestedPredicates = new HashMap<>();
        final ProofPredicateInfo predicateInfo = new ProofPredicateInfo();
        predicateInfo.setName("expiration_date");
        predicateInfo.setPredicateType(">");
        predicateInfo.setPredicateValue("$TODAY-YYYYMMDD");

        final List<AttributeFilter> predicateRestrictions = new ArrayList<>();
        predicateRestrictions.add(new AttributeFilter());
        predicateRestrictions.add(new AttributeFilter());
        predicateRestrictions.get(0).setCredentialDefinitionId("$U-MEMBER");
        predicateRestrictions.get(1).setCredentialDefinitionId("$T-MEMBER");

        predicateInfo.setRestrictions(predicateRestrictions);
        requestedPredicates.put("expiration_date", predicateInfo);

        proofConfig.setRequestedPredicates(requestedPredicates);

        return proofConfig;
    }
}