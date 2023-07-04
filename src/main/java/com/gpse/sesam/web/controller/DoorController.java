package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.DoorService;
import com.gpse.sesam.util.DoorCmdMapper;
import com.gpse.sesam.web.cmd.DoorCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/door")
public class DoorController {

	private final DoorService doorService;

	@Autowired
	public DoorController(final DoorService doorService) {
		this.doorService = doorService;
	}

	@PostMapping("/save")
	public Door save(@RequestBody final DoorCmd door) {
		return doorService.save(DoorCmdMapper.toEntity(door));
	}

	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public Door create(@RequestBody final DoorCmd doorCmd) {
		return doorService.create(doorCmd.getRoomId(), DoorCmdMapper.toEntity(doorCmd));
	}

	@DeleteMapping("/{id:\\d+}")
	public void deleteById(@PathVariable("id") final Long id) {
		doorService.deleteById(id);
	}

	@GetMapping("doorsbyroom/{id:\\d+}")
	public java.util.List<Door> getDoorsByRoomId(@PathVariable("id") final Long id) {
		return doorService.getDoorsByRoomId(id);
	}
}
