package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.location.RoomGroupService;
import com.gpse.sesam.domain.location.RoomGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roomGroups")
//@Secured("EDITOR")
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
}
