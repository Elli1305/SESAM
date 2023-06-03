package com.gpse.sesam.domain.location;

import com.gpse.sesam.web.cmd.RoomGroupCmd;

import java.util.List;
import java.util.Optional;

public interface RoomGroupService {
    List<RoomGroups> getRoomGroups();

    Optional<RoomGroups> getRoomGroups(Long id);

    RoomGroups createRoomGroup(RoomGroupCmd roomGroupCmd);

    void deleteAll();

    void saveAll(Iterable<RoomGroups> roomGroups);

    RoomGroups save(RoomGroups roomGroup);

    void deleteById(Long id);
}
