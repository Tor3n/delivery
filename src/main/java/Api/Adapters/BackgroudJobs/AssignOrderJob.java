package Api.Adapters.BackgroudJobs;

import Core.Commands.SetCourierToOrder.SetCourierToOrderCommand;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssignOrderJob implements Job {
  private static final Logger LOGGER = LoggerFactory.getLogger(AssignOrderJob.class);

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    LOGGER.info("AssignJob");
    SetCourierToOrderCommand courierToOrderCommand = new SetCourierToOrderCommand();
    courierToOrderCommand.command();
  }
}
