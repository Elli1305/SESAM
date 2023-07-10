package com.gpse.sesam.domain.location.roomgroup;

import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.web.cmd.RoomGroupCmd;
import com.gpse.sesam.web.cmd.RoomGroupDoorConfigCmd;
import com.gpse.sesam.web.cmd.TwoWayDoorConfigCmd;

import java.util.List;
import java.util.Optional;

public interface RoomGroupService {
    List<RoomGroups> getRoomGroups();

    List<RoomGroups> getGroupByBuilding(Long id);

    Optional<RoomGroups> getRoomGroups(Long id);

    RoomGroups createRoomGroup(RoomGroupCmd roomGroupCmd);

    void deleteAll();

    RoomGroups getGroupById(Long id);

    void saveAll(Iterable<RoomGroups> roomGroups);

    void makeGroupEdit(RoomGroups oldGroup, String newName, List<Room> newRooms);

    RoomGroups save(RoomGroups roomGroup);

    void deleteById(Long id);

    void setGroupConfig(List<TwoWayDoorConfigCmd> cmds);

    List<RoomGroupDoorConfigCmd> getRoomsAndDoorsByGroupId(Long id);
    List<Room> getRoomsByGroupId(Long id);

}
