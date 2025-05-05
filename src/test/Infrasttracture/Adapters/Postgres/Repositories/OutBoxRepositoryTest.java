package Infrasttracture.Adapters.Postgres.Repositories;

import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.CourierAggregate.CourierStatus;
import Core.Domain.Model.CourierAggregate.Transport;
import Core.Domain.SharedKernel.Location;
import Infrastructure.Adapters.Postgres.Repositories.CourierRepository;
import Infrastructure.Adapters.Postgres.Repositories.OuboxRepository;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OutBoxRepositoryTest {
  OuboxRepository rep = OuboxRepository.getRepository();

  @Test
  public void testAddMessage(){
    JsonObject o = new JsonObject();
    o.addProperty("test", "test2");

    rep.persistMessage("notification", o.toString());
  }

  @Test
  public void testGetMessages(){

    System.out.println(rep.getMessages());
  }


  @Test
  public void testGetMessagesAndThenDelete(){

    List l = rep.getMessages();

    List l2 = new ArrayList();
    l2.add(l.get(0));
    rep.deleteMessages(l2);
  }

}
