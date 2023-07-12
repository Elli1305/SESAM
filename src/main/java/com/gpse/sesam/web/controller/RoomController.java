package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.credential.credentials.internal.CredentialService;
import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomService;
import com.gpse.sesam.util.ConfigCmdMapper;
import com.gpse.sesam.util.DoorCmdMapper;
import com.gpse.sesam.util.RoomCmdMapper;
import com.gpse.sesam.web.cmd.RoomCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;
	private final RoomCmdMapper roomCmdMapper;

    @Autowired
    public RoomController(final RoomService roomService, final CredentialService credentialService) {
        this.roomService = roomService;
		this.roomCmdMapper = new RoomCmdMapper(new DoorCmdMapper(new ConfigCmdMapper(credentialService)));
    }

    @Secured("EDITOR")
    @PostMapping("/save")
    public Room save(@RequestBody final Room room) {
        return roomService.save(room);
    }

    @Secured("EDITOR")
    @DeleteMapping("/{id:\\d+}")
    public void deleteById(@PathVariable("id") final Long id) {
        roomService.deleteById(id);
    }


    @GetMapping("/rooms")
    public List<Room> getRooms() {
        return roomService.getRooms();
	}

	@GetMapping("/{id:\\d+}")
	public RoomCmd getRoom(@PathVariable("id") final Long id) throws ParseException {
		return roomCmdMapper.toCmd(roomService.getRoomById(id).orElseThrow(IllegalArgumentException::new));
    }

    @Secured("EDITOR")
    @GetMapping("/floor/{id:\\d+}")
    public Floor getFloorToRoom(@PathVariable("id") final Long id) {
        return roomService.getFloorToRoom(id);
    }
}
