package com.gpse.sesam.configuration;

import com.gpse.sesam.domain.location.Building;
import com.gpse.sesam.domain.location.Floor;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.domain.location.Room;
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
	private final PasswordEncoder passwordEncoder;

	public InitializeDatabase(LocationService locationService, SesamUserService userService,
							  PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
		this.locationService = locationService;
		this.userService = userService;
	}

	@Override
	public void afterPropertiesSet() {
		locationService.deleteAll();
		userService.deleteAll();

		List<Location> locations = createLocations();
		List<SesamUser> users = createUsers();

		locationService.saveAll(locations);
		userService.saveAll(users);

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
		SesamUser issuer = new SesamUser("issuer@test.de", defaultPassword, "Issuer", "User",
				Collections.singletonList(issuerRole));
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
			floors2.add(new Floor(i % 2, "src/main/resources/citec-gebaeudeplan.png", rooms2.subList(
					i * 5, i * 5 + 5)));
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
}
