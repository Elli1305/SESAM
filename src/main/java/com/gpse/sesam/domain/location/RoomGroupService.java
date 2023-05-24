package com.gpse.sesam.domain.location;

import java.util.List;
import java.util.Optional;

public interface RoomGroupService {
    List<RoomGroups> getRoomGroups();

    Optional<RoomGroups> getRoomGroups(Long id);

    void deleteAll();

    void saveAll(Iterable<RoomGroups> roomGroups);
}
