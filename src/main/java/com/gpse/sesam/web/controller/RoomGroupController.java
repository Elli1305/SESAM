package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.roomgroup.RoomGroupService;
import com.gpse.sesam.domain.location.roomgroup.RoomGroups;
import com.gpse.sesam.web.cmd.RoomGroupCmd;
import com.gpse.sesam.web.cmd.RoomGroupDoorConfigCmd;
import com.gpse.sesam.web.cmd.RoomGroupEditCmd;
import com.gpse.sesam.web.cmd.TwoWayDoorConfigCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roomGroups")
public class RoomGroupController {

    private final RoomGroupService roomGroupService;

    @Autowired
    public RoomGroupController(final RoomGroupService roomGroupService) {
        this.roomGroupService = roomGroupService;
    }

    @GetMapping
    public List<RoomGroups> getAllRoomGroups() {
        return roomGroupService.getRoomGroups();
    }

    @GetMapping("/filter/{id:\\d+}")
    public List<RoomGroups> getGroupsByBuilding(@PathVariable("id") final Long id) {
        return roomGroupService.getGroupByBuilding(id);
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

    @GetMapping("/doorsandrooms/{id}")
    @Secured("EDITOR")
    public List<RoomGroupDoorConfigCmd> getRoomsAndDoorsByGroupId(@PathVariable("id") final Long id) {
        return roomGroupService.getRoomsAndDoorsByGroupId(id);
    }
    @GetMapping("/rooms/{id}")
    @Secured("EDITOR")
    public List<Room> getRoomsByGroupId(@PathVariable("id") final Long id) {
        return roomGroupService.getRoomsByGroupId(id);
    }

    @PostMapping("/saveconfigs")
    @Secured("EDITOR")
    public void updateConfigOfGroup(List<TwoWayDoorConfigCmd> cmds) {
        roomGroupService.setGroupConfig(cmds);
    }
}
