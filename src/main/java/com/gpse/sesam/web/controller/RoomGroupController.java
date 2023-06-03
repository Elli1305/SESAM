package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.RoomGroupService;
import com.gpse.sesam.domain.location.RoomGroups;
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
    public List<RoomGroups> getAllRoomGroups() {
        return roomGroupService.getRoomGroups();
    }

    @PostMapping("/save")
    @Secured("EDITOR")
    public void save(@RequestBody final RoomGroups roomGroup) {
        roomGroupService.save(roomGroup);
    }
    @DeleteMapping("/{id:\\d+}")
    @Secured("EDITOR")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id") final Long id) {
        roomGroupService.deleteById(id);
    }
}
