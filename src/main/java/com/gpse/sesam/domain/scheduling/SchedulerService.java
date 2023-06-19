package com.gpse.sesam.domain.scheduling;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import java.sql.Time;
import java.time.LocalTime;

@Service
public class SchedulerService {
    TaskScheduler taskScheduler;
    SchedulerService(TaskScheduler taskScheduler){
        this.taskScheduler = taskScheduler;
    }

    public void scheduling (Runnable task, LocalTime time){
        //am ende der Zeit wird das auf neue Config. gesetzt
        String cronExpression = String.format("%d %d * * * ?",
             time.getMinute(),
                time.getHour()
        );

        taskScheduler.schedule(task, new CronTrigger(cronExpression));
    }
}
