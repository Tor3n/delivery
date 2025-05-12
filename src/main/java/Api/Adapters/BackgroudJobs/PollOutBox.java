package Api.Adapters.BackgroudJobs;

import Core.Application.KafkaNotifications;
import Core.Domain.Model.OrderAggregate.OrderStatus;
import Infrastructure.Adapters.Postgres.Entity.OutboxMessage;
import Infrastructure.Adapters.Postgres.Repositories.OuboxRepository;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PollOutBox implements Job {


  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    KafkaNotifications notifications = new KafkaNotifications();
    OuboxRepository rep = OuboxRepository.getRepository();

    List<OutboxMessage> outboxMessages = rep.getMessages();
    List<OutboxMessage> removeCandidates = new ArrayList<>();
    for(OutboxMessage message: outboxMessages){
      JSONObject obj = new JSONObject(message.getMessage());
      String uUUID = obj.getString("UUID");
      String oorderStatus = obj.getString("OrderStatus");

      if(notifications.notifyDomainChange(UUID.fromString(uUUID), OrderStatus.of(Integer.getInteger(oorderStatus)))){
        removeCandidates.add(message);
      }
    }
    rep.deleteMessages(removeCandidates);

  }
}
