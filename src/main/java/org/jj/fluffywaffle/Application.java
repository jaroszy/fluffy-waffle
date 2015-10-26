package org.jj.fluffywaffle;

import org.jj.fluffywaffle.jobs.CheckConnectivityJob;
import org.quartz.*;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Application {

    public static void main(String... args) {
        try {
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
            Scheduler sched = schedFact.getScheduler();
            sched.start();
            // define the job and tie it to our HelloJob class
            JobDetail job = newJob(CheckConnectivityJob.class)
                    .withIdentity("connectivityJob", "group1")
                    .build();
            // Trigger the job to run now, and then every 40 seconds
            Trigger trigger = newTrigger()
                    .withIdentity("myTrigger", "group1")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInMinutes(2)
                            .repeatForever())
                    .build();
            // Tell quartz to schedule the job using our trigger
            sched.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
