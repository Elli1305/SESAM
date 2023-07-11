package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.credential.credentials.internal.CredentialService;
import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.DoorService;
import com.gpse.sesam.util.ConfigCmdMapper;
import com.gpse.sesam.util.DoorCmdMapper;
import com.gpse.sesam.web.cmd.DoorCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@Secured("EDITOR")
@RequestMapping("/api/door")
public class DoorController {

	private final DoorService doorService;
	private final DoorCmdMapper doorCmdMapper;

	@Autowired
	public DoorController(final DoorService doorService, final CredentialService credentialService) {
		this.doorService = doorService;
		doorCmdMapper = new DoorCmdMapper(new ConfigCmdMapper(credentialService));
	}

	@PostMapping("/save")
	public Door save(@RequestBody final Door door) {
		return doorService.save(door);
	}


	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public Door create(@RequestBody final DoorCmd doorCmd) {
		return doorService.create(doorCmd.getRoomId(), DoorCmdMapper.toEntity(doorCmd));
	}


	@PostMapping("/update")
	@ResponseStatus(HttpStatus.CREATED)
	public Door update(@RequestBody final DoorCmd doorCmd) {
		return doorService.save(DoorCmdMapper.toEntity(doorCmd));
	}
	@DeleteMapping("/{id:\\d+}")
	public void deleteById(@PathVariable("id") final Long id) {
		doorService.deleteById(id);
	}

	@GetMapping("doorsbyroom/{id:\\d+}")
	public java.util.List<Door> getDoorsByRoomId(@PathVariable("id") final Long id) {
		return doorService.getDoorsByRoomId(id);
	}

	@GetMapping("/{id:\\d+}")
	public DoorCmd getDoorById(@PathVariable("id") final Long id) throws ParseException {
		return doorCmdMapper.toCmd(doorService.findDoorById(id).orElseThrow(IllegalArgumentException::new));
	}
}
