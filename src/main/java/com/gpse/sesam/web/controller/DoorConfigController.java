package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.door.config.DoorConfigService;
import com.gpse.sesam.util.ConfigCmdMapper;
import com.gpse.sesam.web.cmd.ConfigCmd;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doorConfig")
public class DoorConfigController {

	private final DoorConfigService doorConfigurationService;

	public DoorConfigController(final DoorConfigService doorConfigurationService) {
		this.doorConfigurationService = doorConfigurationService;
	}

	@PostMapping("/save")
	@Secured("EDITOR")
	public void save(@RequestBody final ConfigCmd configCmd) {
		doorConfigurationService.sendProofConfig(configCmd.getDoorId(), ConfigCmdMapper.fromCmd(configCmd));
	}
}
