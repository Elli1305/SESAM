package com.gpse.sesam.configuration;

import com.gpse.sesam.domain.colors.ColorTheme;
import com.gpse.sesam.domain.colors.Colors;
import com.gpse.sesam.domain.colors.ColorsService;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredential;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredentialService;
import com.gpse.sesam.domain.credential.credentials.internal.CredentialService;
import com.gpse.sesam.domain.credential.credentials.internal.InternalCredential;
import com.gpse.sesam.domain.credential.issue.issuing.ChecklistEntry;
import com.gpse.sesam.domain.credential.issue.issuing.FormEntry;
import com.gpse.sesam.domain.credential.issue.issuing.FormEntryType;
import com.gpse.sesam.domain.credential.issue.validation.*;
import com.gpse.sesam.domain.filestorage.FileStorageService;
import com.gpse.sesam.domain.location.Coordinate;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.domain.location.door.DoorService;
import com.gpse.sesam.domain.location.building.Building;
import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.config.*;
import com.gpse.sesam.domain.location.door.config.predefined.PredefinedConfig;
import com.gpse.sesam.domain.location.door.config.predefined.PredefinedConfigService;
import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.roomgroup.RoomGroupService;
import com.gpse.sesam.domain.location.roomgroup.RoomGroups;
import com.gpse.sesam.domain.user.SesamUser;
import com.gpse.sesam.domain.user.SesamUserRole;
import com.gpse.sesam.domain.user.SesamUserService;
import com.gpse.sesam.domain.user.issuer.Issuer;
import com.gpse.sesam.web.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Profile("university")
public class InitializeDatabaseUniversity implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(InitializeDatabaseLocal.class);

    private final LocationService locationService;

    private final SesamUserService userService;

    private final CredentialService credentialService;
    private final ColorsService colorsService;

    private final FileStorageService fileStorageService;

    private final DoorService doorService;

    private final PasswordEncoder passwordEncoder;

    private final ExternalCredentialService externalCredentialService;

    private final PredefinedConfigService predefinedConfigService;

    private final RoomGroupService roomGroupService;

    @SuppressWarnings("ParameterNumber")
    public InitializeDatabaseUniversity(final LocationService locationService, final SesamUserService userService,
                                        final CredentialService credentialService, final ColorsService colorsService,
                                        FileStorageService fileStorageService, final PasswordEncoder passwordEncoder,
                                        DoorService doorService, ExternalCredentialService externalCredentialService,
                                        PredefinedConfigService predefinedConfigService,
                                        RoomGroupService roomGroupService) {
        this.credentialService = credentialService;
        this.colorsService = colorsService;
        this.fileStorageService = fileStorageService;
        this.passwordEncoder = passwordEncoder;
        this.locationService = locationService;
        this.userService = userService;
        this.doorService = doorService;
        this.externalCredentialService = externalCredentialService;
        this.predefinedConfigService = predefinedConfigService;
        this.roomGroupService = roomGroupService;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        final List<Colors> colors = createColors();
        final List<InternalCredential> credentials = createCredentials();
        final List<SesamUser> users = createUsers();
        final List<Location> locations = createLocations();
        final List<ExternalCredential> externalCredentials = createExternals();
        final List<RoomGroups> roomGroups = createRoomGroup(locations);
        final List<PredefinedConfig> predefinedConfigs = createPredefinedConfigList();

        colorsService.saveAll(colors);
        setLogo();
        credentialService.saveAll(credentials);
        userService.saveAll(users);
        locationService.saveAll(locations);
        doorService.save(locations.get(0).getBuildings().get(0).getFloors().get(0).getRooms().get(0).getDoors().get(0));
        externalCredentialService.saveAll(externalCredentials);
        roomGroupService.saveAll(roomGroups);
        predefinedConfigService.saveAll(predefinedConfigs);
    }

    private List<Colors> createColors() {
        final Colors defaultLight = new Colors();
        defaultLight.setDefaultColors(true);
        defaultLight.setTheme(ColorTheme.LIGHT);
        setLightColors(defaultLight);
        defaultLight.setLogoPath("Universität_Bielefeld_Logo_white.svg");

        final Colors defaultDark = new Colors();
        defaultDark.setDefaultColors(true);
        defaultDark.setTheme(ColorTheme.DARK);
        setDarkColors(defaultDark);
        defaultDark.setLogoPath("Universität_Bielefeld_Logo_black.svg");

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

    private final List<RoomGroups> createRoomGroup(List<Location> locations) {
        List<RoomGroups> groups = new ArrayList<>();
        groups.add(new RoomGroups("Labor", locations.get(0).getBuildings().get(0).getFloors().get(0).getRooms(),
                locations.get(0).getBuildings().get(0)));

        return groups;
    }

    private void setLightColors(final Colors defaultColors) {
        defaultColors.setLogoPath("/Logo.svg");
        defaultColors.setBgC("#ffffff");
        defaultColors.setTextC("#000000");
        defaultColors.setPrimaryColor("#1b805f");
        defaultColors.setSecondary("#8dbfaf");
        defaultColors.setAccent("#ffffff");
        defaultColors.setDark("#808080");
        defaultColors.setLight("#9e9e9e");
        defaultColors.setPositive("#dcdcdc");
        defaultColors.setNegative("#505050");
        defaultColors.setInfo("#224f7d");
        defaultColors.setWarning("#c08329");
    }

    private void setDarkColors(final Colors defaultColors) {
        defaultColors.setLogoPath("/Logo-Dark.svg");
        defaultColors.setBgC("#303030");
        defaultColors.setTextC("#ffffff");
        defaultColors.setPrimaryColor("#1b805f");
        defaultColors.setSecondary("#8dbfaf");
        defaultColors.setAccent("#000000");
        defaultColors.setDark("#808080");
        defaultColors.setLight("#9e9e9e");
        defaultColors.setPositive("#dcdcdc");
        defaultColors.setNegative("#505050");
        defaultColors.setInfo("#224f7d");
        defaultColors.setWarning("#c08329");
    }

    private void setLogo() {
        try {
            fileStorageService.reset(ColorTheme.LIGHT);
            fileStorageService.reset(ColorTheme.DARK);
        } catch (FileStorageException ignored) {
            LOG.warn("Unable to reset fileStorageService.");
        }
    }

    private List<SesamUser> createUsers() {
        //User Roles
        final SesamUserRole adminRole = new SesamUserRole(SesamUserRole.AttainableRole.ADMINISTRATOR);
        adminRole.setGranted(true);


        List<SesamUserRole> editorAndIssuer = new ArrayList<>();

        final SesamUserRole editorRole = new SesamUserRole(SesamUserRole.AttainableRole.EDITOR);
        editorRole.setGranted(true);
        final SesamUserRole issuerRole = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
        issuerRole.setGranted(true);

        editorAndIssuer.add(editorRole);
        editorAndIssuer.add(issuerRole);

        //Users
        final String defaultPassword = passwordEncoder.encode("Hallo123!");

        final SesamUser admin = new SesamUser("admin@test.de", defaultPassword, "UBI", "Admin",
                Collections.singletonList(adminRole));
        final SesamUser editorIssuer = new Issuer("jörn@sesam.de", defaultPassword, "Jörn", "Mühlenkamp",
                editorAndIssuer, new Room("0.409"));

        return List.of(admin, editorIssuer);
    }


    private List<InternalCredential> createCredentials() {

        List<InternalCredential> internalCredentials = new ArrayList<>();

        internalCredentials.add(new InternalCredential(
                "U-Member", "1.0", "$U-MEMBER",
                "university", formMember(), checklistMember()));
        internalCredentials.add(new InternalCredential(
                "U-Training", "1.0", "$U-TRAINING",
                "university", formTraining(), checklistTraining()));

        return internalCredentials;
    }

    private List<ExternalCredential> createExternals() {
        List<ExternalCredential> externalCredentials = new ArrayList<>();

        ExternalCredential ttraining = new ExternalCredential("T-Training", "1.0", "$T-TRAINING", formTraining());
        ExternalCredential tmember = new ExternalCredential("T-Member", "1.0", "$T-MEMBER", formMember());

        externalCredentials.add(ttraining);
        externalCredentials.add(tmember);

        return externalCredentials;
    }

    private List<Location> createLocations() {

        //Room 0.114
        List<Coordinate> coordinates = new ArrayList<>();

        coordinates.add(new Coordinate(new BigDecimal("55.45"), new BigDecimal("69.95")));
        coordinates.add(new Coordinate(new BigDecimal("21.62"), new BigDecimal("69.95")));
        coordinates.add(new Coordinate(new BigDecimal("21.62"), new BigDecimal("59.06")));
        coordinates.add(new Coordinate(new BigDecimal("12.64"), new BigDecimal("59.06")));
        coordinates.add(new Coordinate(new BigDecimal("12.64"), new BigDecimal("46.62")));
        coordinates.add(new Coordinate(new BigDecimal("55.45"), new BigDecimal("46.62")));

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

        doorCoordinates.add(new Coordinate(new BigDecimal("23.91"), new BigDecimal("69.96")));
        doorCoordinates.add(new Coordinate(new BigDecimal("27.11"), new BigDecimal("69.96")));

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
        final FormEntry id = new FormEntry("ID", FormEntryType.NUMBER, "id",
                getIdValidationRules());
        final FormEntry firstName = new FormEntry("Vorname", FormEntryType.TEXT, "first_name",
                getFirstNameValidationRules());
        final FormEntry lastName = new FormEntry("Nachname", FormEntryType.TEXT, "last_name",
                getLastNameValidationRules());
        final FormEntry birthDate = new FormEntry("Geburtstagsdatum", FormEntryType.DATE, "birth_date",
                getBirthDateValidationRules());
        final FormEntry date = new FormEntry("Ablaufdatum", FormEntryType.DATE, "expiration_date",
                getDateValidationRules());
        form.add(id);
        form.add(firstName);
        form.add(lastName);
        form.add(birthDate);
        form.add(date);
        return form;
    }

    public List<FormEntry> formTraining() {
        final List<FormEntry> form = new ArrayList<>();
        final FormEntry firstName = new FormEntry("Vorname", FormEntryType.TEXT, "first_name",
                getFirstNameValidationRules());
        final FormEntry lastName = new FormEntry("Nachname", FormEntryType.TEXT, "last_name",
                getLastNameValidationRules());
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
        proofConfig.setDescription("Präsentieren Sie ein U-Member oder T-Member Credential.");

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

    private ProofConfig createPredefinedConfig() {
        final ProofConfig proofConfig = new ProofConfig();
        proofConfig.setDescription("Präsentieren Sie ein U-Member Credential mit dem Geburtsdatum vor 2005.");

        final Map<String, ProofPredicateInfo> requestedPredicates = new HashMap<>();
        final ProofPredicateInfo predicateInfo = new ProofPredicateInfo();
        predicateInfo.setName("expiration_date");
        predicateInfo.setPredicateType(">");
        predicateInfo.setPredicateValue("$TODAY-YYYYMMDD");
        final ProofPredicateInfo predicateInfo2 = new ProofPredicateInfo();
        predicateInfo2.setName("birth_date");
        predicateInfo2.setPredicateType("<");
        predicateInfo2.setPredicateValue("20050101");

        final List<AttributeFilter> predicateRestrictions = new ArrayList<>();
        predicateRestrictions.add(new AttributeFilter());
        predicateRestrictions.get(0).setCredentialDefinitionId("$U-MEMBER");


        final List<AttributeFilter> predicateRestrictions2 = new ArrayList<>();
        predicateRestrictions2.add(new AttributeFilter());
        predicateRestrictions2.get(0).setCredentialDefinitionId("$U-MEMBER");

        predicateInfo.setRestrictions(predicateRestrictions);
        predicateInfo2.setRestrictions(predicateRestrictions2);
        requestedPredicates.put("expiration_date", predicateInfo);
        requestedPredicates.put("birth_date", predicateInfo2);

        proofConfig.setRequestedPredicates(requestedPredicates);


        return proofConfig;
    }

    private List<PredefinedConfig> createPredefinedConfigList() {
        final ProofConfig config = createPredefinedConfig();
        final ProofConfig config2 = createPredefinedConfig();

        TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
        twoWayDoorConfig.setBaseConfig(true);
        twoWayDoorConfig.setProofConfigIn(config);
        twoWayDoorConfig.setProofConfigOut(config2);

        final PredefinedConfig predefinedConfig = new PredefinedConfig("U-Member vor 2005 geboren",
                List.of(twoWayDoorConfig));

        return List.of(predefinedConfig);
    }

    private List<AbstractValidationRule> getIdValidationRules() {
        final List<AbstractValidationRule> validationRules = new ArrayList<>();
        validationRules.add(new ComparisonRule(ComparisonType.GREATER_EQUAL, "0"));
        return validationRules;
    }

    private List<AbstractValidationRule> getFirstNameValidationRules() {
        final List<AbstractValidationRule> validationRules = new ArrayList<>();
        validationRules.add(new RegExRule(
                "^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅ"
                        + "ĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$",
                "Wähle eine realen Name / Choose a real name"));
        validationRules.add(new LengthRule(ComparisonType.LESS_THAN, 50));
        return validationRules;
    }

    private List<AbstractValidationRule> getLastNameValidationRules() {
        final List<AbstractValidationRule> validationRules = new ArrayList<>();
        validationRules.add(new RegExRule(
                "^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅ"
                        + "ĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$",
                "Wähle eine realen Name / Choose a real name"));
        validationRules.add(new LengthRule(ComparisonType.LESS_THAN, 50));
        return validationRules;
    }

    private List<AbstractValidationRule> getBirthDateValidationRules() {
        final List<AbstractValidationRule> validationRules = new ArrayList<>();
        validationRules.add(new ComparisonRule(ComparisonType.LESS_THAN, true, "Ablaufdatum"));
        return validationRules;
    }

    private List<AbstractValidationRule> getDateValidationRules() {
        final List<AbstractValidationRule> validationRules = new ArrayList<>();
        validationRules.add(new ComparisonRule(ComparisonType.GREATER_THAN, true, "Geburtstagsdatum"));
        return validationRules;
    }

}