package com.gpse.sesam.domain.location.door;

import com.gpse.sesam.domain.location.door.config.DoorConfigService;
import com.gpse.sesam.domain.location.door.config.ProofConfig;
import com.gpse.sesam.domain.scheduling.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoorServiceImpl implements DoorService {

	private final DoorRepository doorRepository;
	private  final DoorConfigService doorConfigService;

	private final SchedulerService schedulerService;

	@Autowired
	public DoorServiceImpl(final DoorRepository doorRepository, DoorConfigService doorConfigService, SchedulerService schedulerService) {
		this.doorRepository = doorRepository;
		this.doorConfigService = doorConfigService;
		this.schedulerService = schedulerService;
	}

	@Override
	public void deleteById(Long id) {
		doorRepository.deleteById(id);
	}

	@Override
	public Door save(Door door) {
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
}
