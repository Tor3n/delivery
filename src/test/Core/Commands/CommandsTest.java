package Core.Commands;

import Core.Commands.CreateOrder.CreateOrderCommand;
import Core.Commands.MoveCourier.MoveCouriersCommand;
import Core.Commands.SetCourierToOrder.SetCourierToOrderCommand;
import Core.Domain.SharedKernel.Location;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class CommandsTest {

  @Test
  public void createOrderCommandTest(){
    CreateOrderCommand c = new CreateOrderCommand(UUID.randomUUID(), new Location(10,10));
    c.command();
  }

  @Test
  public void setCourierToOrder(){
    //ORDERstatus =1 courierstatus = 2
    SetCourierToOrderCommand c = new SetCourierToOrderCommand();
    c.command();
  }

  @Test
  public void moveCouriers(){
    MoveCouriersCommand c = new MoveCouriersCommand();
    c.command();
  }

}
