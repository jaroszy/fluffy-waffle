package org.jj.fluffywaffle.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.util.Date;


/**
 * Created by julka on 24.10.15.
 */
public class JobRescheduler {

    public static void delayCurrentJob(JobExecutionContext context, Date newStartTime) {

        if (!newStartTime.after(now())) {
            return;
        }

        Trigger currentTrigger = context.getTrigger();

        try {
            context.getScheduler().rescheduleJob(currentTrigger.getKey(), getDelayedTrigger(currentTrigger, newStartTime));

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private static Trigger getDelayedTrigger(Trigger currentTrigger, Date newStartTime){

        TriggerBuilder delayedTriggerBuilder = currentTrigger.getTriggerBuilder();

        return delayedTriggerBuilder.startAt(newStartTime).build();
    }

    private static Date now(){
        return new Date();
    }
}
