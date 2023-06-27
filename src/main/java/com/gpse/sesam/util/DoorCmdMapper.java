package com.gpse.sesam.util;

import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.web.cmd.DoorCmd;
import com.gpse.sesam.web.cmd.DoorConfigCmd;
import com.gpse.sesam.web.cmd.DoorConfigViewCmd;

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

		for (final DoorConfigCmd doorConfigCmd : doorCmd.getProofConfigIn()) {
			door.addProofConfigIn(configCmdMapper.fromCmd(doorConfigCmd));
		}


		for (final DoorConfigCmd doorConfigCmd : doorCmd.getProofConfigOut()) {
			door.addProofConfigOut(configCmdMapper.fromCmd(doorConfigCmd));
		}

		return door;
	}
}
