package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.RoomGroupService;
import com.gpse.sesam.domain.location.RoomGroups;
import com.gpse.sesam.web.cmd.RoomGroupCmd;
import com.gpse.sesam.web.cmd.RoomGroupEditCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roomGroups")
@Secured("EDITOR")
public class RoomGroupController {

    private final RoomGroupService roomGroupService;

    @Autowired
    public RoomGroupController(final RoomGroupService roomGroupService) {
        this.roomGroupService = roomGroupService;
    }

    @GetMapping
    @Secured("EDITOR")
    public List<RoomGroups> getAllRoomGroups() {
        return roomGroupService.getRoomGroups();
    }

    @PostMapping("/save")
    @Secured("EDITOR")
    public RoomGroups save(@RequestBody final RoomGroups roomGroup) {
        return roomGroupService.save(roomGroup);
    }

    @PostMapping("/newgroup")
    @Secured("EDITOR")
    @ResponseStatus(HttpStatus.CREATED)
    public RoomGroups createRoomGroup(@RequestBody final RoomGroupCmd roomGroupCmd) {
        return roomGroupService.createRoomGroup(roomGroupCmd);
    }

    @Secured("EDITOR")
    @PostMapping("/edit_group")
    @ResponseStatus(HttpStatus.OK)
    public void makeGroupEdit(@RequestBody final RoomGroupEditCmd roomGroupEditCmd) {
        final RoomGroups roomGroup = roomGroupService.getGroupById(roomGroupEditCmd.getId());
        roomGroupService.makeGroupEdit(roomGroup,
                roomGroupEditCmd.getName(),
                roomGroupEditCmd.getRooms());
    }

    @DeleteMapping("/{id:\\d+}")
    @Secured("EDITOR")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id") final Long id) {
        roomGroupService.deleteById(id);
    }
}
