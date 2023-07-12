package com.gpse.sesam.util;

import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.web.cmd.DoorCmd;
import com.gpse.sesam.web.cmd.RoomCmd;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public final class RoomCmdMapper {

	private DoorCmdMapper doorCmdMapper;

	public RoomCmdMapper(DoorCmdMapper doorCmdMapper) {
		this.doorCmdMapper = doorCmdMapper;
	}

	public RoomCmd toCmd(Room room) throws ParseException {
		RoomCmd roomCmd = new RoomCmd();
		roomCmd.setId(room.getId());
		roomCmd.setName(room.getName());
		roomCmd.setCoordinates(room.getCoordinates());
		List<DoorCmd> doorCmdList = new ArrayList<>();
		for (Door door : room.getDoors()) {
			doorCmdList.add(doorCmdMapper.toCmd(door));
		}
		roomCmd.setDoors(doorCmdList);
		return roomCmd;
	}
}
