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
@SuppressWarnings("avoidnestedblocks")
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
    private final CategoryService categoryService;

    @SuppressWarnings("ParameterNumber")
    public InitializeDatabaseUniversity(final LocationService locationService, final SesamUserService userService,
                                        final CredentialService credentialService, final ColorsService colorsService,
                                        FileStorageService fileStorageService, final PasswordEncoder passwordEncoder,
                                        DoorService doorService, ExternalCredentialService externalCredentialService,
                                        PredefinedConfigService predefinedConfigService,
                                        RoomGroupService roomGroupService, CategoryService categoryService) {
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
        this.categoryService = categoryService;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        final List<Colors> colors = createColors();
        final List<InternalCredential> credentials = createCredentials();
        final List<SesamUser> users = createUsers(credentials);
        final List<Location> locations = createLocations();
        final List<ExternalCredential> externalCredentials = createExternals();
        final List<RoomGroups> roomGroups = createRoomGroup(locations);
        final List<PredefinedConfig> predefinedConfigs = createPredefinedConfigList();
        final List<Category> categories = createCategories(credentials, externalCredentials);

        colorsService.saveAll(colors);
        setLogo();
        credentialService.saveAll(credentials);
        userService.saveAll(users);
        locationService.saveAll(locations);
        doorService.save(locations.get(0).getBuildings().get(0).getFloors().get(0).getRooms().get(0).getDoors().get(0));
        externalCredentialService.saveAll(externalCredentials);
        roomGroupService.saveAll(roomGroups);
        predefinedConfigService.saveAll(predefinedConfigs);
        categoryService.saveAll(categories);
    }

    private List<Colors> createColors() {
        final Colors defaultLight = new Colors();
        defaultLight.setDefaultColors(true);
        defaultLight.setCorporateName("Universität Bielefeld");
        defaultLight.setTheme(ColorTheme.LIGHT);
        setLightColors(defaultLight);
        defaultLight.setLogoPath("Universität_Bielefeld_Logo_white.svg");

        final Colors defaultDark = new Colors();
        defaultDark.setDefaultColors(true);
        defaultDark.setCorporateName("Universität Bielefeld");
        defaultDark.setTheme(ColorTheme.DARK);
        setDarkColors(defaultDark);
        defaultDark.setLogoPath("Universität_Bielefeld_Logo_black.svg");

        final Colors currentLight = new Colors();
        currentLight.setDefaultColors(false);
        currentLight.setCorporateName("Universität Bielefeld");
        currentLight.setTheme(ColorTheme.LIGHT);
        setLightColors(currentLight);

        final Colors currentDark = new Colors();
        currentDark.setDefaultColors(false);
        currentDark.setCorporateName("Universität Bielefeld");
        currentDark.setTheme(ColorTheme.DARK);
        setDarkColors(currentDark);

        final List<Colors> colors = new ArrayList<>();
        colors.add(defaultLight);
        colors.add(defaultDark);
        colors.add(currentLight);
        colors.add(currentDark);

        return colors;
    }

    private List<RoomGroups> createRoomGroup(List<Location> locations) {
        final List<RoomGroups> groups = new ArrayList<>();

        groups.add(new RoomGroups("Labore",
                locations.stream().filter(location -> location.getName().equals("Bielefeld")).toList().get(0)
                        .getBuildings().get(0).getFloors().get(0)
                        .getRooms().stream().filter(room ->
                                room.getName().equals("0.114")
                                        || room.getName().equals("0.117")
                                        || room.getName().equals("0.414")
                                        || room.getName().equals("0.112")).toList(),
                locations.get(0).getBuildings().get(0)));
        groups.add(new RoomGroups("Büros",
                locations.stream().filter(location -> location.getName().equals("Bielefeld")).toList().get(0)
                        .getBuildings().get(0).getFloors().get(0)
                        .getRooms().stream().filter(room ->
                                room.getName().equals("0.115")
                                        || room.getName().equals("0.116")
                                        || room.getName().equals("0.214")).toList(),
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
        defaultColors.setInfo("#1061b3");
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
        defaultColors.setInfo("#1061b3");
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

    private List<SesamUser> createUsers(List<InternalCredential> credentials) {

        final List<SesamUser> users = new ArrayList<>();

        //Password
        final String defaultPassword = passwordEncoder.encode("Hallo123!");

        //Users
        users.add(new SesamUser("hubert@sesam.de", defaultPassword, "Hubert", "Müller",
                List.of(new SesamUserRole(SesamUserRole.AttainableRole.ADMINISTRATOR, true))));

        users.add(new Issuer("jörn@sesam.de", defaultPassword, "Jörn", "Mühlenkamp",
                List.of(
                        new SesamUserRole(SesamUserRole.AttainableRole.EDITOR, true),
                        new SesamUserRole(SesamUserRole.AttainableRole.ISSUER, true)),
                new Room("0.109"),
                List.of(credentials.get(0))));
        credentials.get(0).setIssuer(List.of((Issuer) users.get(users.size() - 1)));

        users.add(new Issuer("jutta@sesam.de", defaultPassword, "Jutta", "Bergmann",
                List.of(new SesamUserRole(SesamUserRole.AttainableRole.ISSUER, true)),
                new Room("0.106"),
                List.of(credentials.get(0),
                        credentials.get(1))));
        credentials.get(0).setIssuer(List.of((Issuer) users.get(users.size() - 1)));
        credentials.get(1).setIssuer(List.of((Issuer) users.get(users.size() - 1)));

        users.add(new Issuer("kelvin@sesam.de", defaultPassword, "Kelvin", "Matthews",
                List.of(new SesamUserRole(SesamUserRole.AttainableRole.ISSUER, true)),
                new Room("0.107"),
                List.of(credentials.get(0),
                        credentials.get(2))));
        credentials.get(0).setIssuer(List.of((Issuer) users.get(users.size() - 1)));
        credentials.get(2).setIssuer(List.of((Issuer) users.get(users.size() - 1)));

        users.add(new Issuer("hannah@sesam.de", defaultPassword, "Hannah", "Bauer",
                List.of(new SesamUserRole(SesamUserRole.AttainableRole.ISSUER, false))));

        users.add(new SesamUser("moritz@sesam.de", defaultPassword, "Moritz", "Brockner",
                List.of(new SesamUserRole(SesamUserRole.AttainableRole.EDITOR, false))));

        return users;
    }


    private List<InternalCredential> createCredentials() {

        List<InternalCredential> internalCredentials = new ArrayList<>();

        internalCredentials.add(new InternalCredential(
                "U-Member", "1.0", "$U-MEMBER",
                "university", formMember(), checklistMember()));
        internalCredentials.add(new InternalCredential(
                "U-Training", "1.0", "$U-TRAINING",
                "university", formTraining(), checklistTraining()));

        internalCredentials.add(new InternalCredential(
                "U-Lab", "1.0", "$U-Lab",
                "university", formTraining(), checklistTraining()));

        return internalCredentials;
    }

    private List<Category> createCategories(List<InternalCredential> internal, List<ExternalCredential> external) {
        List<Category> categories = new ArrayList<>();
        Category lab = new Category("lab-trained");
        lab.addCredential(internal.get(2));
        internal.get(2).setCategory(lab);

        for (ExternalCredential externalCredential: external) {
            if (externalCredential.getName().equals("T-Lab")) {
                lab.addExternalCredential(externalCredential);
                externalCredential.setCategory(lab);
            }
        }
        categories.add(lab);

        return categories;
    }

    private List<ExternalCredential> createExternals() {
        List<ExternalCredential> externalCredentials = new ArrayList<>();

        ExternalCredential ttraining = new ExternalCredential("T-Training", "1.0",
                "$T-TRAINING", formTraining());
        ExternalCredential tmember = new ExternalCredential("T-Member",
                "1.0", "$T-MEMBER", formMember());

        ExternalCredential tlab = new ExternalCredential("T-Lab",
                "1.0", "$T-Lab", formMember());

        externalCredentials.add(ttraining);
        externalCredentials.add(tmember);
        externalCredentials.add(tlab);

        return externalCredentials;
    }

    private List<Location> createLocations() {

        //Room 0.114
        final Room r114 = new Room("0.114");
        {
            List<Coordinate> coordinates = new ArrayList<>();

            coordinates.add(new Coordinate(new BigDecimal("55.45"), new BigDecimal("69.95")));
            coordinates.add(new Coordinate(new BigDecimal("21.62"), new BigDecimal("69.95")));
            coordinates.add(new Coordinate(new BigDecimal("21.62"), new BigDecimal("59.06")));
            coordinates.add(new Coordinate(new BigDecimal("12.64"), new BigDecimal("59.06")));
            coordinates.add(new Coordinate(new BigDecimal("12.64"), new BigDecimal("46.62")));
            coordinates.add(new Coordinate(new BigDecimal("55.45"), new BigDecimal("46.62")));

            r114.setCoordinates(coordinates);

            //Door 1
            {
                List<Coordinate> doorCoordinates = new ArrayList<>();
                ProofConfig proofConfig = createProofConfig();
                ProofConfig proofConfig2 = createProofConfig();

                TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
                twoWayDoorConfig.setBaseConfig(true);
                twoWayDoorConfig.setProofConfigIn(proofConfig);
                twoWayDoorConfig.setProofConfigOut(proofConfig2);

                doorCoordinates.add(new Coordinate(new BigDecimal("23.91"), new BigDecimal("69.95")));
                doorCoordinates.add(new Coordinate(new BigDecimal("27.11"), new BigDecimal("69.95")));

                Door door = new Door("0.114.1", doorCoordinates);
                door.setDoorConfigs(List.of(twoWayDoorConfig));

                r114.addDoor(door);
            }

            //Door 2
            {
                List<Coordinate> doorCoordinates = new ArrayList<>();
                ProofConfig proofConfig = createProofConfig();
                ProofConfig proofConfig2 = createProofConfig();

                TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
                twoWayDoorConfig.setBaseConfig(true);
                twoWayDoorConfig.setProofConfigIn(proofConfig);
                twoWayDoorConfig.setProofConfigOut(proofConfig2);

                doorCoordinates.add(new Coordinate(new BigDecimal("55.45"), new BigDecimal("56.01")));
                doorCoordinates.add(new Coordinate(new BigDecimal("55.45"), new BigDecimal("50.52")));

                Door door = new Door("0.114.2", doorCoordinates);
                door.setDoorConfigs(List.of(twoWayDoorConfig));

                r114.addDoor(door);
            }

            //Door 3
            {
                List<Coordinate> doorCoordinates = new ArrayList<>();
                ProofConfig proofConfig = createProofConfig();
                ProofConfig proofConfig2 = createProofConfig();

                TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
                twoWayDoorConfig.setBaseConfig(true);
                twoWayDoorConfig.setProofConfigIn(proofConfig);
                twoWayDoorConfig.setProofConfigOut(proofConfig2);

                doorCoordinates.add(new Coordinate(new BigDecimal("43.26"), new BigDecimal("46.62")));
                doorCoordinates.add(new Coordinate(new BigDecimal("49.41"), new BigDecimal("46.62")));

                Door door = new Door("0.114.3", doorCoordinates);
                door.setDoorConfigs(List.of(twoWayDoorConfig));

                r114.addDoor(door);
            }

            //Door 4
            {
                List<Coordinate> doorCoordinates = new ArrayList<>();
                ProofConfig proofConfig = createProofConfig();
                ProofConfig proofConfig2 = createProofConfig();

                TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
                twoWayDoorConfig.setBaseConfig(true);
                twoWayDoorConfig.setProofConfigIn(proofConfig);
                twoWayDoorConfig.setProofConfigOut(proofConfig2);

                doorCoordinates.add(new Coordinate(new BigDecimal("15.38"), new BigDecimal("46.62")));
                doorCoordinates.add(new Coordinate(new BigDecimal("18.58"), new BigDecimal("46.62")));

                Door door = new Door("0.114.3", doorCoordinates);
                door.setDoorConfigs(List.of(twoWayDoorConfig));

                r114.addDoor(door);
            }
        }

        //Room 0.115
        final Room r115 = new Room("0.115");
        {
            List<Coordinate> coordinates = new ArrayList<>();

            coordinates.add(new Coordinate(new BigDecimal("12.64"), new BigDecimal("59.06")));
            coordinates.add(new Coordinate(new BigDecimal("12.64"), new BigDecimal("69.95")));
            coordinates.add(new Coordinate(new BigDecimal("21.62"), new BigDecimal("69.95")));
            coordinates.add(new Coordinate(new BigDecimal("21.62"), new BigDecimal("59.06")));

            r115.setCoordinates(coordinates);

            //Door 1
            {
                List<Coordinate> doorCoordinates = new ArrayList<>();
                ProofConfig proofConfig = createProofConfig();
                ProofConfig proofConfig2 = createProofConfig();

                TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
                twoWayDoorConfig.setBaseConfig(true);
                twoWayDoorConfig.setProofConfigIn(proofConfig);
                twoWayDoorConfig.setProofConfigOut(proofConfig2);

                doorCoordinates.add(new Coordinate(new BigDecimal("15.39"), new BigDecimal("69.96")));
                doorCoordinates.add(new Coordinate(new BigDecimal("18.59"), new BigDecimal("69.96")));

                Door door = new Door("0.115.1", doorCoordinates);
                door.setDoorConfigs(List.of(twoWayDoorConfig));

                r115.addDoor(door);
            }
        }

        //Room 0.116
        final Room r116 = new Room("0.116");
        {
            List<Coordinate> coordinates = new ArrayList<>();

            coordinates.add(new Coordinate(new BigDecimal("30.04"), new BigDecimal("75.83")));
            coordinates.add(new Coordinate(new BigDecimal("30.04"), new BigDecimal("89.72")));
            coordinates.add(new Coordinate(new BigDecimal("38.04"), new BigDecimal("89.72")));
            coordinates.add(new Coordinate(new BigDecimal("38.04"), new BigDecimal("75.83")));

            r116.setCoordinates(coordinates);

            //Door 1
            {
                List<Coordinate> doorCoordinates = new ArrayList<>();
                ProofConfig proofConfig = createProofConfig();
                ProofConfig proofConfig2 = createProofConfig();

                TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
                twoWayDoorConfig.setBaseConfig(true);
                twoWayDoorConfig.setProofConfigIn(proofConfig);
                twoWayDoorConfig.setProofConfigOut(proofConfig2);

                doorCoordinates.add(new Coordinate(new BigDecimal("33.06"), new BigDecimal("75.83")));
                doorCoordinates.add(new Coordinate(new BigDecimal("36.19"), new BigDecimal("75.83")));

                Door door = new Door("0.116.1", doorCoordinates);
                door.setDoorConfigs(List.of(twoWayDoorConfig));

                r116.addDoor(door);
            }
        }

        //Room 0.117
        final Room r117 = new Room("0.117");
        {
            List<Coordinate> coordinates = new ArrayList<>();

            coordinates.add(new Coordinate(new BigDecimal("-12.00"), new BigDecimal("75.83")));
            coordinates.add(new Coordinate(new BigDecimal("-12.00"), new BigDecimal("89.72")));
            coordinates.add(new Coordinate(new BigDecimal("30.04"), new BigDecimal("89.72")));
            coordinates.add(new Coordinate(new BigDecimal("30.04"), new BigDecimal("75.83")));

            r117.setCoordinates(coordinates);

            //Door 1
            {
                List<Coordinate> doorCoordinates = new ArrayList<>();
                ProofConfig proofConfig = createProofConfig();
                ProofConfig proofConfig2 = createProofConfig();

                TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
                twoWayDoorConfig.setBaseConfig(true);
                twoWayDoorConfig.setProofConfigIn(proofConfig);
                twoWayDoorConfig.setProofConfigOut(proofConfig2);

                doorCoordinates.add(new Coordinate(new BigDecimal("-4.65"), new BigDecimal("75.83")));
                doorCoordinates.add(new Coordinate(new BigDecimal("-1.88"), new BigDecimal("75.83")));

                Door door = new Door("0.117.1", doorCoordinates);
                door.setDoorConfigs(List.of(twoWayDoorConfig));

                r117.addDoor(door);
            }

            //Door 2
            {
                List<Coordinate> doorCoordinates = new ArrayList<>();
                ProofConfig proofConfig = createProofConfig();
                ProofConfig proofConfig2 = createProofConfig();

                TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
                twoWayDoorConfig.setBaseConfig(true);
                twoWayDoorConfig.setProofConfigIn(proofConfig);
                twoWayDoorConfig.setProofConfigOut(proofConfig2);

                doorCoordinates.add(new Coordinate(new BigDecimal("24.64"), new BigDecimal("75.83")));
                doorCoordinates.add(new Coordinate(new BigDecimal("27.85"), new BigDecimal("75.83")));

                Door door = new Door("0.117.1", doorCoordinates);
                door.setDoorConfigs(List.of(twoWayDoorConfig));

                r117.addDoor(door);
            }
        }

        //Room 0.414
        final Room r414 = new Room("0.414");
        {
            List<Coordinate> coordinates = new ArrayList<>();

            coordinates.add(new Coordinate(new BigDecimal("-39.74"), new BigDecimal("15.16")));
            coordinates.add(new Coordinate(new BigDecimal("-39.74"), new BigDecimal("50.87")));
            coordinates.add(new Coordinate(new BigDecimal("-20.16"), new BigDecimal("50.87")));
            coordinates.add(new Coordinate(new BigDecimal("-20.16"), new BigDecimal("15.16")));

            r414.setCoordinates(coordinates);

            //Door 1
            {
                List<Coordinate> doorCoordinates = new ArrayList<>();
                ProofConfig proofConfig = createProofConfig();
                ProofConfig proofConfig2 = createProofConfig();

                TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
                twoWayDoorConfig.setBaseConfig(true);
                twoWayDoorConfig.setProofConfigIn(proofConfig);
                twoWayDoorConfig.setProofConfigOut(proofConfig2);

                doorCoordinates.add(new Coordinate(new BigDecimal("-20.16"), new BigDecimal("46.90")));
                doorCoordinates.add(new Coordinate(new BigDecimal("-20.16"), new BigDecimal("41.40")));

                Door door = new Door("0.414.1", doorCoordinates);
                door.setDoorConfigs(List.of(twoWayDoorConfig));

                r414.addDoor(door);
            }
        }

        //Room 0.112
        final Room r112 = new Room("0.112");
        {
            List<Coordinate> coordinates = new ArrayList<>();

            coordinates.add(new Coordinate(new BigDecimal("12.69"), new BigDecimal("-6.02")));
            coordinates.add(new Coordinate(new BigDecimal("12.69"), new BigDecimal("21.56")));
            coordinates.add(new Coordinate(new BigDecimal("55.47"), new BigDecimal("21.56")));
            coordinates.add(new Coordinate(new BigDecimal("55.47"), new BigDecimal("-6.02")));

            r112.setCoordinates(coordinates);

            //Door 1
            {
                List<Coordinate> doorCoordinates = new ArrayList<>();
                ProofConfig proofConfig = createProofConfig();
                ProofConfig proofConfig2 = createProofConfig();

                TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
                twoWayDoorConfig.setBaseConfig(true);
                twoWayDoorConfig.setProofConfigIn(proofConfig);
                twoWayDoorConfig.setProofConfigOut(proofConfig2);

                doorCoordinates.add(new Coordinate(new BigDecimal("25.02"), new BigDecimal("21.56")));
                doorCoordinates.add(new Coordinate(new BigDecimal("37.33"), new BigDecimal("21.56")));

                Door door = new Door("0.112.1", doorCoordinates);
                door.setDoorConfigs(List.of(twoWayDoorConfig));

                r112.addDoor(door);
            }

            //Door 2
            {
                List<Coordinate> doorCoordinates = new ArrayList<>();
                ProofConfig proofConfig = createProofConfig();
                ProofConfig proofConfig2 = createProofConfig();

                TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
                twoWayDoorConfig.setBaseConfig(true);
                twoWayDoorConfig.setProofConfigIn(proofConfig);
                twoWayDoorConfig.setProofConfigOut(proofConfig2);

                doorCoordinates.add(new Coordinate(new BigDecimal("55.47"), new BigDecimal("0.73")));
                doorCoordinates.add(new Coordinate(new BigDecimal("55.47"), new BigDecimal("-5.00")));

                Door door = new Door("0.112.2", doorCoordinates);
                door.setDoorConfigs(List.of(twoWayDoorConfig));

                r112.addDoor(door);
            }

            //Door 3
            {
                List<Coordinate> doorCoordinates = new ArrayList<>();
                ProofConfig proofConfig = createProofConfig();
                ProofConfig proofConfig2 = createProofConfig();

                TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
                twoWayDoorConfig.setBaseConfig(true);
                twoWayDoorConfig.setProofConfigIn(proofConfig);
                twoWayDoorConfig.setProofConfigOut(proofConfig2);

                doorCoordinates.add(new Coordinate(new BigDecimal("21.86"), new BigDecimal("-6.02")));
                doorCoordinates.add(new Coordinate(new BigDecimal("25.01"), new BigDecimal("-6.02")));

                Door door = new Door("0.112.3", doorCoordinates);
                door.setDoorConfigs(List.of(twoWayDoorConfig));

                r112.addDoor(door);
            }
        }

        //Room 0.214
        final Room r214 = new Room("0.214");
        {
            List<Coordinate> coordinates = new ArrayList<>();

            coordinates.add(new Coordinate(new BigDecimal("62.86"), new BigDecimal("-61.94")));
            coordinates.add(new Coordinate(new BigDecimal("62.86"), new BigDecimal("-54.17")));
            coordinates.add(new Coordinate(new BigDecimal("72.23"), new BigDecimal("-54.17")));
            coordinates.add(new Coordinate(new BigDecimal("72.23"), new BigDecimal("-61.94")));

            r214.setCoordinates(coordinates);

            //Door 1
            {
                List<Coordinate> doorCoordinates = new ArrayList<>();
                ProofConfig proofConfig = createProofConfig();
                ProofConfig proofConfig2 = createProofConfig();

                TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
                twoWayDoorConfig.setBaseConfig(true);
                twoWayDoorConfig.setProofConfigIn(proofConfig);
                twoWayDoorConfig.setProofConfigOut(proofConfig2);

                doorCoordinates.add(new Coordinate(new BigDecimal("62.86"), new BigDecimal("-57.49")));
                doorCoordinates.add(new Coordinate(new BigDecimal("62.86"), new BigDecimal("-60.32")));

                Door door = new Door("0.214.1", doorCoordinates);
                door.setDoorConfigs(List.of(twoWayDoorConfig));

                r214.addDoor(door);
            }
        }

        //Floor
        final Floor floor = new Floor(0, "/citec-gebaeudeplan.svg");
        floor.addRoom(r114);
        floor.addRoom(r115);
        floor.addRoom(r116);
        floor.addRoom(r117);
        floor.addRoom(r414);
        floor.addRoom(r112);
        floor.addRoom(r214);
        final Floor floor2 = new Floor(0, "/citec-gebaeudeplan.svg");
        floor.addRoom(new Room("Room"));
        final Floor floor3 = new Floor(0, "/citec-gebaeudeplan.svg");
        floor.addRoom(new Room("Room"));
        final Floor floor4 = new Floor(0, "/citec-gebaeudeplan.svg");
        floor.addRoom(new Room("Room"));
        final Floor floor5 = new Floor(0, "/citec-gebaeudeplan.svg");
        floor.addRoom(new Room("Room"));

        //Building
        final Building building = new Building("CITEC");
        building.addFloor(floor);
        final Building building2 = new Building("CITEC");
        building2.addFloor(floor2);
        final Building building3 = new Building("CITEC");
        building3.addFloor(floor3);
        final Building building4 = new Building("CITEC");
        building4.addFloor(floor4);
        final Building building5 = new Building("CITEC");
        building5.addFloor(floor5);

        //Location
        List<Location> locations = new ArrayList<>();

        final Location bielefeld = new Location("Bielefeld");
        bielefeld.addBuilding(building);
        final Location hamburg = new Location("Hamburg");
        hamburg.addBuilding(building2);
        final Location koeln = new Location("Köln");
        koeln.addBuilding(building3);
        final Location hannover = new Location("Hannover");
        hannover.addBuilding(building4);
        final Location bremen = new Location("Bremen");
        bremen.addBuilding(building5);

        locations.add(bielefeld);
        locations.add(hamburg);
        locations.add(koeln);
        locations.add(hannover);
        locations.add(bremen);

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