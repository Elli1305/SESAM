package com.gpse.sesam.domain.location.door;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gpse.sesam.domain.location.door.config.DoorConfigService;
import com.gpse.sesam.domain.location.door.config.ProofConfig;
import com.gpse.sesam.domain.scheduling.SchedulerService;
import com.gpse.sesam.domain.location.door.config.DoorConfigService;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomRepository;
import com.gpse.sesam.domain.location.room.RoomService;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DoorServiceImpl implements DoorService {

	private final DoorRepository doorRepository;
	private  final DoorConfigService doorConfigService;

	private final SchedulerService schedulerService;

	private final RoomService roomService;

	private final RoomRepository roomRepository;


	@Autowired
	public DoorServiceImpl(final DoorRepository doorRepository, DoorConfigService doorConfigService, SchedulerService schedulerService, final RoomService roomService,
						   RoomRepository roomRepository) {
		this.doorRepository = doorRepository;
		this.doorConfigService = doorConfigService;
		this.schedulerService = schedulerService;
		this.roomService = roomService;
		this.roomRepository = roomRepository;
	}

	@Override
	public void deleteById(final Long id) {
		doorRepository.deleteById(id);
	}

	@Override
	public Door save(final Door door) {
		for(ProofConfig proofConfig: door.getProofConfigs()){
			schedulerService.scheduling(()->doorConfigService.sendProofConfig(door.getName()+"_"+door.getId(), proofConfig), proofConfig.getStartTime());
			for(ProofConfig proofConfig2: door.getProofConfigs()){
				if(proofConfig.getEndTime()== proofConfig2.getStartTime()){
					schedulerService.scheduling(()->doorConfigService.sendProofConfig(door.getName()+"_"+door.getId(), proofConfig2), proofConfig.getStartTime());
				} else{
					for(ProofConfig proofConfigbase: door.getProofConfigs()){
						if(proofConfigbase.isBaseConfig()){
							schedulerService.scheduling(()->doorConfigService.sendProofConfig(door.getName()+"_"+door.getId(), proofConfigbase), proofConfig.getStartTime());
						}
					}

				}
			}
			schedulerService.scheduling(()->doorConfigService.sendProofConfig(door.getName()+"_"+door.getId(), proofConfig), proofConfig.getEndTime());

			// startTime = endTime?
		}
		return doorRepository.save(door);
	}

	@Override
	public Optional<Door> findDoorById(Long id) {
		return doorRepository.findById(id);
	}

	@Override
	@Transactional
	public Door create(final Long roomId, final Door door) {

		final Door savedDoor = doorRepository.save(door);

		final Room room = roomService.getById(roomId);
		room.addDoor(savedDoor);

		roomService.save(room);

		doorConfigService.sendProofConfig(savedDoor.getName() + "_" + savedDoor.getId() + "_in",
				door.getProofConfigIn()
						.get(0));
		doorConfigService.sendProofConfig(savedDoor.getName() + "_" + savedDoor.getId() + "_out",
				door.getProofConfigOut()
						.get(0));
		return savedDoor;
	}

	@Override
	public List<Door> getDoorsByRoomId(Long id) {
		Optional <Room> room = roomRepository.findById(id);
		if (room.isPresent()) {
			return room.get().getDoors();
		}
		return null;
	}
}
