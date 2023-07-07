package com.gpse.sesam.domain.location.roomgroup;

import com.gpse.sesam.domain.credential.credentials.internal.CredentialService;
import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.DoorRepository;
import com.gpse.sesam.domain.location.door.config.DoorConfigService;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.util.ConfigCmdMapper;
import com.gpse.sesam.web.cmd.RoomGroupCmd;
import com.gpse.sesam.web.cmd.RoomGroupDoorConfigCmd;
import com.gpse.sesam.web.cmd.TwoWayDoorConfigCmd;
import com.gpse.sesam.web.exception.ConflictException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service

public class RoomGroupServiceImpl implements RoomGroupService {

	private final RoomGroupRepository roomGroupRepository;
	private final DoorConfigService doorConfigurationService;

	private final DoorRepository doorRepository;

	@Autowired
	public RoomGroupServiceImpl(final RoomGroupRepository roomGroupRepository,
								DoorConfigService doorConfigurationService,
								DoorRepository doorRepository) {
		this.roomGroupRepository = roomGroupRepository;
		this.doorConfigurationService = doorConfigurationService;
		this.doorRepository = doorRepository;
	}

	@Override
	public RoomGroups createRoomGroup(final RoomGroupCmd roomGroupCmd) {
		final RoomGroups newRoomgroup = new RoomGroups(
				roomGroupCmd.getName(),
				roomGroupCmd.getRooms(),
				roomGroupCmd.getBuilding()
		);
		try {
			return roomGroupRepository.save(newRoomgroup);
		} catch (final DataIntegrityViolationException e) {
			throw new ConflictException("A group with this id already exists", e);
		}
	}

	@Override
	public List<RoomGroups> getRoomGroups() {
		final List<RoomGroups> roomGroups = new ArrayList<>();
		roomGroupRepository.findAll().forEach(roomGroups::add);
		return roomGroups;
	}

	@Override
	public List<RoomGroups> getGroupByBuilding(Long bId) {
		final List<RoomGroups> allRoomGroups = getRoomGroups();
		List<RoomGroups> filteredGroups = new ArrayList<>();
		for (RoomGroups roomGroup : allRoomGroups) {
			if (Objects.equals(roomGroup.getBuilding().getId(), bId)) {
				filteredGroups.add(roomGroup);
			}
		}
		return filteredGroups;
	}

	@Override
	public Optional<RoomGroups> getRoomGroups(final Long id) {
		return roomGroupRepository.findById(id);
	}

	@Override
	public RoomGroups getGroupById(Long id) {
		return roomGroupRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
	}

	@Override
	public void deleteAll() {
		roomGroupRepository.deleteAll();
	}

	@Override
	public void saveAll(final Iterable<RoomGroups> roomGroups) {
		roomGroupRepository.saveAll(roomGroups);
	}

	@Override
	public RoomGroups save(RoomGroups roomGroup) {
		return roomGroupRepository.save(roomGroup);
	}

	@Override
	public void makeGroupEdit(final RoomGroups editGroup, final String newName, final List<Room> newRooms) {
		editGroup.setName(newName);
		editGroup.setRooms(newRooms);

		roomGroupRepository.save(editGroup);
	}

	@Override
	public void deleteById(Long id) {
		roomGroupRepository.deleteById(id);
	}

	@Override
	public void setGroupConfig(List<TwoWayDoorConfigCmd> cmds) {
		for (TwoWayDoorConfigCmd cmd : cmds) {
			Optional<Door> door = doorRepository.findById(Long.valueOf(cmd.getDoorConfigIn().getDoorId()));
            // TODO change when merged with master
//            if (door.isPresent()) {
//                doorConfigurationService.sendProofConfig(cmd.getDoorConfigIn()
//                        .getDoorId(), ConfigCmdMapper.fromCmd(cmd.getDoorConfigIn()));
//                doorConfigurationService.sendProofConfig(cmd.getDoorConfigOut()
//                        .getDoorId(), ConfigCmdMapper.fromCmd(cmd.getDoorConfigOut()));
//                door.get().setProofConfigIn(List.of(ConfigCmdMapper.fromCmd(cmd.getDoorConfigIn())));
//                door.get().setProofConfigOut(List.of(ConfigCmdMapper.fromCmd(cmd.getDoorConfigOut())));
//                doorRepository.save(door.get());
//            }
		}
	}

	@Override
	public List<RoomGroupDoorConfigCmd> getRoomsAndDoorsByGroupId(Long id) {
		Optional<RoomGroups> roomGroup = roomGroupRepository.findById(id);
		List<RoomGroupDoorConfigCmd> cmds = new ArrayList<>();
		if (roomGroup.isPresent()) {
			for (Room room : roomGroup.get().getRooms()) {
				java.util.List<Door> doors = new ArrayList<>();
				String roomName = room.getName();
				Long roomId = room.getId();
				for (Door door : room.getDoors()) {
					doors.add(door);
				}
				cmds.add(new RoomGroupDoorConfigCmd(roomId, roomName, doors));
			}
		}
		return cmds;
	}

	@Override
	public List<Room> getRoomsByGroupId(Long id) {
		Optional<RoomGroups> roomGroup = roomGroupRepository.findById(id);
		List<Room> rooms = new ArrayList<>();
		if (roomGroup.isPresent()) {
			for (Room room : roomGroup.get().getRooms()) {
				rooms.add(room);
			}
		}
		return rooms;
	}
}
