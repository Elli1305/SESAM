package com.gpse.sesam.util;

import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.TwoWayDoorConfig;
import com.gpse.sesam.web.cmd.DoorCmd;
import com.gpse.sesam.web.cmd.TwoWayDoorConfigCmd;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public final class DoorCmdMapper {
	private ConfigCmdMapper configCmdMapper;

	public DoorCmdMapper(ConfigCmdMapper configCmdMapper) {

		this.configCmdMapper = configCmdMapper;
	}

	public Door toEntity(final DoorCmd doorCmd) {
		final Door door = new Door();

		door.setId(doorCmd.getId());
		door.setName(doorCmd.getName());
		door.setCoordinates(doorCmd.getCoordinates());

		List<TwoWayDoorConfig> doorConfigs = new ArrayList<>();

		for (TwoWayDoorConfigCmd twoWayDoorConfigCmd : doorCmd.getDoorConfigCmds()) {
			TwoWayDoorConfig twoWayDoorConfig = new TwoWayDoorConfig();
			twoWayDoorConfig.setEndTime(twoWayDoorConfigCmd.getEndTime());
			twoWayDoorConfig.setStartTime(twoWayDoorConfigCmd.getStartTime());
			twoWayDoorConfig.setBaseConfig(twoWayDoorConfigCmd.isBaseConfig());
			twoWayDoorConfig.setId(twoWayDoorConfigCmd.getId());
			twoWayDoorConfig.setProofConfigIn(ConfigCmdMapper.fromCmd(twoWayDoorConfigCmd.getDoorConfigIn()));
			twoWayDoorConfig.setProofConfigOut(ConfigCmdMapper.fromCmd(twoWayDoorConfigCmd.getDoorConfigOut()));
			doorConfigs.add(twoWayDoorConfig);
		}


		door.setDoorConfigs(doorConfigs);

		return door;
	}

	public DoorCmd toCmd(final Door door) throws ParseException {
		final DoorCmd doorCmd = new DoorCmd();
		doorCmd.setId(door.getId());
		doorCmd.setName(door.getName());
		doorCmd.setCoordinates(door.getCoordinates());

		List<TwoWayDoorConfigCmd> doorConfigCmds = new ArrayList<>();

		for (TwoWayDoorConfig twoWayDoorConfig : door.getDoorConfigs()) {
			TwoWayDoorConfigCmd twoWayDoorConfigCmd = new TwoWayDoorConfigCmd();
			twoWayDoorConfigCmd.setEndTime(twoWayDoorConfig.getEndTime());
			twoWayDoorConfigCmd.setStartTime(twoWayDoorConfig.getStartTime());
			twoWayDoorConfigCmd.setBaseConfig(twoWayDoorConfig.isBaseConfig());
			twoWayDoorConfigCmd.setId(twoWayDoorConfig.getId());
			twoWayDoorConfigCmd.setDoorConfigIn(configCmdMapper.toCmd(twoWayDoorConfig.getProofConfigIn()));
			twoWayDoorConfigCmd.setDoorConfigOut(configCmdMapper.toCmd(twoWayDoorConfig.getProofConfigOut()));
			doorConfigCmds.add(twoWayDoorConfigCmd);
		}

		doorCmd.setDoorConfigCmds(doorConfigCmds);

		return doorCmd;
	}
}
