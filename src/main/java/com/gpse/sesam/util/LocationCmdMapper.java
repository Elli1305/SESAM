package com.gpse.sesam.util;

import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.building.Building;
import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.config.ProofConfig;
import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.web.cmd.BuildingCmd;
import com.gpse.sesam.web.cmd.DoorCmd;
import com.gpse.sesam.web.cmd.DoorConfigCmd;
import com.gpse.sesam.web.cmd.DoorConfigViewCmd;
import com.gpse.sesam.web.cmd.DoorViewCmd;
import com.gpse.sesam.web.cmd.FloorCmd;
import com.gpse.sesam.web.cmd.LocationCmd;
import com.gpse.sesam.web.cmd.RoomCmd;
import com.gpse.sesam.web.exception.InvalidDoorConfiguration;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public final class LocationCmdMapper {

	private ConfigCmdMapper configCmdMapper;

	public LocationCmdMapper(ConfigCmdMapper configCmdMapper) {
		this.configCmdMapper = configCmdMapper;
	}

	public LocationCmd toCmd(final Location location) {
		final LocationCmd locationCmd = new LocationCmd();

		locationCmd.setId(location.getId());
		locationCmd.setName(location.getName());
		List<BuildingCmd> buildingCmds = new ArrayList<>();
		for (Building building : location.getBuildings()) {
			buildingCmds.add(toBuildingCmd(building));
		}
		locationCmd.setBuildings(buildingCmds);


		return locationCmd;
	}

	private BuildingCmd toBuildingCmd(Building building) {
		BuildingCmd buildingCmd = new BuildingCmd();
		buildingCmd.setId(building.getId());
		buildingCmd.setName(building.getName());
		List<FloorCmd> floorCmds = new ArrayList<>();
		for (Floor floor : building.getFloors()) {
			floorCmds.add(toFloorCmd(floor));
		}
		buildingCmd.setFloors(floorCmds);
		return buildingCmd;
	}

	private FloorCmd toFloorCmd(Floor floor) {
		FloorCmd floorCmd = new FloorCmd();
		floorCmd.setId(floor.getId());
		floorCmd.setFloorPlanPath(floor.getFloorPlanPath());
		floorCmd.setFloorLevel(floor.getFloorLevel());
		List<RoomCmd> roomCmds = new ArrayList<>();
		for (Room room : floor.getRooms()) {
			roomCmds.add(toRoomCmd(room));
		}
		floorCmd.setRooms(roomCmds);
		return floorCmd;
	}

	private RoomCmd toRoomCmd(Room room) {
		RoomCmd roomCmd = new RoomCmd();
		roomCmd.setId(room.getId());
		roomCmd.setName(room.getName());
		List<DoorViewCmd> doorCmds = new ArrayList<>();
		for (Door door : room.getDoors()) {
			doorCmds.add(toDoorCmd(door));
		}
		roomCmd.setCoordinates(room.getCoordinates());
		roomCmd.setDoors(doorCmds);
		return roomCmd;
	}

	private DoorViewCmd toDoorCmd(Door door) {
		DoorViewCmd doorCmd = new DoorViewCmd();
		doorCmd.setId(door.getId());
		doorCmd.setName(door.getName());
		doorCmd.setCoordinates(door.getCoordinates());

		List<DoorConfigViewCmd> doorConfigViewCmdsIn = new ArrayList<>();
		for (ProofConfig proofConfig : door.getProofConfigIn()) {
			try {
				doorConfigViewCmdsIn.add(configCmdMapper.fromEntity(proofConfig));
			} catch (ParseException e) {
				throw new InvalidDoorConfiguration("could not read door configuration", e);
			}
		}
		doorCmd.setProofConfigIn(doorConfigViewCmdsIn);

		List<DoorConfigViewCmd> doorConfigViewCmdsOut = new ArrayList<>();
		for (ProofConfig proofConfig : door.getProofConfigOut()) {
			try {
				doorConfigViewCmdsOut.add(configCmdMapper.fromEntity(proofConfig));
			} catch (ParseException e) {
				throw new InvalidDoorConfiguration("could not read door configuration", e);
			}
		}
		doorCmd.setProofConfigIn(doorConfigViewCmdsOut);
		return doorCmd;
	}
}
