package Core.Domain.Model.OrderAggregate;

import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.SharedKernel.Location;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

  private UUID orderUUID = UUID.randomUUID();

  @Test
  public void orderCompletedTest(){
    Courier courier = new Courier("Vasily", "bike", 2, new Location(1,1));
    Order order = new Order(orderUUID, new Location(5,5));

    assertFalse(order.completeOrder());

    order.assignCourier(courier);

    assertTrue(order.completeOrder());

  }


}
