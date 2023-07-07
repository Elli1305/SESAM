package com.gpse.sesam.domain.scheduling;

import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.door.Door;
import com.gpse.sesam.domain.location.door.DoorService;
import com.gpse.sesam.domain.location.door.TwoWayDoorConfig;
import com.gpse.sesam.domain.location.door.config.DoorConfigService;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class SchedulerService {
    //TaskScheduler taskScheduler;
    DoorService doorService;
    DoorConfigService doorConfigService;
    SchedulerService(DoorService doorService, DoorConfigService doorConfigService){
    //    this.taskScheduler = taskScheduler;
        this.doorService = doorService;
        this.doorConfigService = doorConfigService;


    }

/*    public void scheduling (Runnable task, LocalTime time){
        //am ende der Zeit wird das auf neue Config. gesetzt
        String cronExpression = String.format("%d %d * * * ?",
             time.getMinute(),
                time.getHour()
        );

        taskScheduler.schedule(task, new CronTrigger(cronExpression));
    }*/

    @Scheduled(fixedRate = 60000)
    public void sendDoorConfig (){
        final List<Door> doors = doorService.getDoors();

        for(Door door: doors){
            if(door.getDoorConfigs().size() > 1){
                        for(TwoWayDoorConfig proofConfig: door.getDoorConfigs()){
                            LocalTime currentTime = LocalTime.now();

                            if (currentTime.isAfter(proofConfig.getStartTime()) && currentTime.isBefore(proofConfig.getEndTime())) {
                                doorConfigService.sendProofConfig(door.getName()+"_"+door.getId()+"_in", proofConfig.getProofConfigIn());
                                doorConfigService.sendProofConfig(door.getName()+"_"+door.getId()+"_out", proofConfig.getProofConfigOut());
                            }

                        }
                }
            }
        }




}
