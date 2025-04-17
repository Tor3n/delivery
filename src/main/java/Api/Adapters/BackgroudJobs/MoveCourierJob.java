package Api.Adapters.BackgroudJobs;

import Core.Commands.MoveCourier.MoveCouriersCommand;
import Core.Configuration;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveCourierJob implements Job {
  private static final Logger LOGGER = LoggerFactory.getLogger(MoveCourierJob.class);

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    LOGGER.info("MoveJob");
    MoveCouriersCommand couriersCommand = new MoveCouriersCommand();
    couriersCommand.command();
  }
}
