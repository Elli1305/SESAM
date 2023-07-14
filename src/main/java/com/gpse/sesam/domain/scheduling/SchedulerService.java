package com.gpse.sesam.domain.scheduling;

import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.DoorService;
import com.gpse.sesam.domain.location.door.config.DoorConfigService;
import com.gpse.sesam.domain.location.door.config.TwoWayDoorConfig;
import com.gpse.sesam.util.ActiveConfigUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementierung des SchedulerService, der Operationen zum Scheduling enthält
 */

@Service
public class SchedulerService {
    DoorService doorService;
    DoorConfigService doorConfigService;

    /**
     * Konstruktoren des SchedulerService
     * @param doorService DoorService
     * @param doorConfigService Doorconfigservice
     */
    SchedulerService(DoorService doorService, DoorConfigService doorConfigService) {
        this.doorService = doorService;
        this.doorConfigService = doorConfigService;


    }

    /**
     * Versenden der DoorConfig zur richtigen Zeit
     */
    @Scheduled(fixedRate = 60000)
    public void sendDoorConfig() {
        final List<Door> doors = doorService.getDoors();

        for (Door door : doors) {
            if (door.getDoorConfigs().size() > 1) {
                TwoWayDoorConfig activeConfig = ActiveConfigUtil.getCurrentConfig(door.getDoorConfigs());
                if (activeConfig != null) {
                    doorConfigService.sendProofConfig(door.getName() + "_" + door.getId()
                            + "_in", activeConfig.getProofConfigIn());
                    doorConfigService.sendProofConfig(door.getName() + "_" + door.getId()
                            + "_out", activeConfig.getProofConfigOut());
                }
            }
        }
    }
}

