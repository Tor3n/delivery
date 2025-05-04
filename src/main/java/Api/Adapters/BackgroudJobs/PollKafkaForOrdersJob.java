package Api.Adapters.BackgroudJobs;

import Api.Adapters.Kafka.JsonSimpleKafkaAdapterImpl;
import Api.Adapters.Kafka.KafkaPort;
import Core.Commands.Commandable;
import kafka.Kafka;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PollKafkaForOrdersJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(PollKafkaForOrdersJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Commandable pollKafka = new JsonSimpleKafkaAdapterImpl();
        pollKafka.command();
    }
}
