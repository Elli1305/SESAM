package com.gpse.sesam.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.domain.colors.ColorTheme;
import com.gpse.sesam.domain.colors.Colors;
import com.gpse.sesam.domain.colors.ColorsService;
import com.gpse.sesam.domain.credential.category.Category;
import com.gpse.sesam.domain.credential.category.CategoryService;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredential;
import com.gpse.sesam.domain.credential.credentials.internal.CredentialService;
import com.gpse.sesam.domain.credential.credentials.internal.InternalCredential;
import com.gpse.sesam.domain.credential.issue.issuing.ChecklistEntry;
import com.gpse.sesam.domain.credential.issue.issuing.FormEntry;
import com.gpse.sesam.domain.credential.issue.issuing.FormEntryType;
import com.gpse.sesam.domain.credential.issue.validation.AbstractValidationRule;
import com.gpse.sesam.domain.credential.issue.validation.ComparisonRule;
import com.gpse.sesam.domain.credential.issue.validation.ComparisonType;
import com.gpse.sesam.domain.credential.issue.validation.LengthRule;
import com.gpse.sesam.domain.credential.issue.validation.RegExRule;
import com.gpse.sesam.domain.filestorage.FileStorageService;
import com.gpse.sesam.domain.location.Coordinate;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.domain.location.building.Building;
import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.config.AttributeFilter;
import com.gpse.sesam.domain.location.door.config.ProofConfig;
import com.gpse.sesam.domain.location.door.config.ProofPredicateInfo;
import com.gpse.sesam.domain.location.door.config.TwoWayDoorConfig;
import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.roomgroup.RoomGroupService;
import com.gpse.sesam.domain.location.roomgroup.RoomGroups;
import com.gpse.sesam.domain.user.SesamUser;
import com.gpse.sesam.domain.user.SesamUserRole;
import com.gpse.sesam.domain.user.SesamUserService;
import com.gpse.sesam.domain.user.issuer.Issuer;
import com.gpse.sesam.util.GeoJsonParser;
import com.gpse.sesam.web.exception.FileStorageException;
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
@Profile("test")
public class InitializeDatabaseLocal implements InitializingBean {
	private static final String DEFAULT_PASSWORD = "Hallo123!";

	private static final Logger LOG = LoggerFactory.getLogger(InitializeDatabaseLocal.class);

	private final LocationService locationService;
	private final RoomGroupService roomGroupService;

	private final SesamUserService userService;

	private final CredentialService credentialService;
	private final ColorsService colorsService;

	private final FileStorageService fileStorageService;

	private final CategoryService categoryService;

	private final PasswordEncoder passwordEncoder;

	private final List<Location> locationsList = new ArrayList<>();

	@SuppressWarnings("ParameterNumber")
	public InitializeDatabaseLocal(final LocationService locationService, final SesamUserService userService,
								   final CredentialService credentialService, final ColorsService colorsService,
								   final CategoryService categoryService, final PasswordEncoder passwordEncoder,
								   final RoomGroupService roomGroupService,
								   final FileStorageService fileStorageService) {
		this.credentialService = credentialService;
		this.colorsService = colorsService;
		this.categoryService = categoryService;
		this.passwordEncoder = passwordEncoder;
		this.locationService = locationService;
		this.userService = userService;
		this.roomGroupService = roomGroupService;
		this.fileStorageService = fileStorageService;
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
		setLogo();
		locationService.saveAll(locations);
		credentialService.saveAll(credentials);
		locationService.saveAll(locationsList);
		categoryService.saveAll(categories);
		userService.saveAll(users);
		roomGroupService.saveAll(roomGroups);
	}

	private List<Colors> createColors() {
		final Colors defaultLight = new Colors();
		defaultLight.setDefaultColors(true);
		defaultLight.setCorporateName("SESAM");
		defaultLight.setTheme(ColorTheme.LIGHT);
		setLightColors(defaultLight);
		defaultLight.setLogoPath("T_logo_white.svg");

		final Colors defaultDark = new Colors();
		defaultDark.setDefaultColors(true);
		defaultDark.setCorporateName("SESAM");
		defaultDark.setTheme(ColorTheme.DARK);
		setDarkColors(defaultDark);
		defaultDark.setLogoPath("T_logo_black.svg");

		final Colors currentLight = new Colors();
		currentLight.setDefaultColors(false);
		currentLight.setCorporateName("SESAM");
		currentLight.setTheme(ColorTheme.LIGHT);
		setLightColors(currentLight);

		final Colors currentDark = new Colors();
		currentDark.setDefaultColors(false);
		currentDark.setCorporateName("SESAM");
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

	private List<SesamUser> createUsers(final List<InternalCredential> credentials) {
		final SesamUserRole adminRole = new SesamUserRole(SesamUserRole.AttainableRole.ADMINISTRATOR);
		adminRole.setGranted(true);
		final SesamUserRole issuerRole = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole.setGranted(true);
		final SesamUserRole editorRole = new SesamUserRole(SesamUserRole.AttainableRole.EDITOR);
		editorRole.setGranted(true);
		final String defaultPassword = passwordEncoder.encode(DEFAULT_PASSWORD);
		final SesamUser admin = new SesamUser("admin@test.de", defaultPassword, "Admin", "User",
				List.of(adminRole));
		final SesamUser editor = new SesamUser("editor@test.de", defaultPassword, "Editor", "User",
				List.of(editorRole));
		final SesamUser user = new SesamUser("user@test.de", defaultPassword, "Test", "User",
				Collections.emptyList());

		final SesamUserRole issuerRole20 = new SesamUserRole(SesamUserRole.AttainableRole.ISSUER);
		issuerRole20.setGranted(true);
		final SesamUserRole editorRole21 = new SesamUserRole(SesamUserRole.AttainableRole.EDITOR);
		editorRole21.setGranted(true);

		final SesamUser jana = new Issuer("jana@test.de", defaultPassword, "Jana", "Editor-Issuer",
				List.of(editorRole21, issuerRole20), null);


		return List.of(admin, editor, user, jana);
	}

	private List<RoomGroups> roomGroups(final List<Location> locations) {
		final List<RoomGroups> groups = new ArrayList<>();

		groups.add(new RoomGroups("Labore", locations.stream().filter(location -> location.getName().equals("Bielefeld")).toList().get(0).getBuildings().get(0).getFloors().get(0).getRooms().stream().filter(room -> room.getName().equals("0.114") || room.getName().equals("0.117") || room.getName().equals("0.414") || room.getName().equals("0.112")).toList(),
				locations.get(0).getBuildings().get(0)));
		groups.add(new RoomGroups("Büros", locations.stream().filter(location -> location.getName().equals("Bielefeld")).toList().get(0).getBuildings().get(0).getFloors().get(0).getRooms().stream().filter(room -> room.getName().equals("0.115") || room.getName().equals("0.116") || room.getName().equals("0.214")).toList(),
				locations.get(0).getBuildings().get(0)));

		return groups;
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
		floor2.addRoom(new Room("Room"));
		final Floor floor3 = new Floor(0, "/citec-gebaeudeplan.svg");
		floor3.addRoom(new Room("Room"));
		final Floor floor4 = new Floor(0, "/citec-gebaeudeplan.svg");
		floor4.addRoom(new Room("Room"));
		final Floor floor5 = new Floor(0, "/citec-gebaeudeplan.svg");
		floor5.addRoom(new Room("Room"));

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
		final Location bremen = new Location("Bremen");
		bremen.addBuilding(building5);

		locations.add(bielefeld);
		locations.add(hamburg);
		locations.add(koeln);
		locations.add(bremen);

		return locations;
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

	private List<FormEntry> form() {
		final List<FormEntry> form = new ArrayList<>();
		final FormEntry id = new FormEntry("ID", FormEntryType.NUMBER, "id", getIdValidationRules());
		final FormEntry firstName = new FormEntry(
				"Vorname", FormEntryType.TEXT, "first_name", getFirstNameValidationRules());
		final FormEntry lastName = new FormEntry(
				"Nachname", FormEntryType.TEXT, "last_name", getLastNameValidationRules());
		final FormEntry birthDate = new FormEntry(
				"Geburtstagsdatum", FormEntryType.DATE, "birth_date", getBirthDateValidationRules());
		final FormEntry date = new FormEntry(
				"Ablaufdatum", FormEntryType.DATE, "expiration_date", getDateValidationRules());
		form.add(id);
		form.add(firstName);
		form.add(lastName);
		form.add(birthDate);
		form.add(date);
		return form;
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

	private List<ChecklistEntry> checklist() {
		final List<ChecklistEntry> checklist = new ArrayList<>();
		checklist.add(new ChecklistEntry("Wurde der Kurs erfolgreich abgeschlossen?"));
		checklist.add(new ChecklistEntry("Wurde der notwendige Nachweis erbracht?"));
		return checklist;
	}

	private List<InternalCredential> createCredentials() {
		List<InternalCredential> internalCredentials = new ArrayList<>();

		internalCredentials.add(new InternalCredential(
				"U-Member", "1.0", "$U-MEMBER",
				"university", form(), checklist()));
		internalCredentials.add(new InternalCredential(
				"T-Member", "1.0", "$T-MEMBER",
				"tlabs", form(), checklist()));

		return internalCredentials;
	}

	private List<Category> createCredentialCategories() {
		List<Category> categories = new ArrayList<>();

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
