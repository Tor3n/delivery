package Api.Adapters.BackgroudJobs;

import org.quartz.*;
import org.quartz.impl.*;

import javax.inject.Singleton;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Singleton
public class BackgroundTasks {

  public BackgroundTasks() throws SchedulerException {
    start();
  }

  public static void start() throws SchedulerException {
    SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    Scheduler scheduler = schedulerFactory.getScheduler();
    scheduler.start();

    JobDetail jobDetail = newJob(AssignOrderJob.class).
            withIdentity("assignOrder", "assign").
            build();

    Trigger trigger = newTrigger().
            withIdentity("triggerAssign", "assign").
            startNow().
            withSchedule(simpleSchedule().
                    withIntervalInSeconds(5).
                    repeatForever()).
            build();

    JobDetail jobDetail2 = newJob(MoveCourierJob.class).
            withIdentity("moveCourier", "move1").
            build();

    Trigger trigger2 = newTrigger().
            withIdentity("triggerMove", "move").
            startNow().
            withSchedule(simpleSchedule().
                    withIntervalInSeconds(10).
                    repeatForever()).
            build();

    scheduler.scheduleJob(jobDetail, trigger);
    scheduler.scheduleJob(jobDetail2, trigger2);

  }
}
