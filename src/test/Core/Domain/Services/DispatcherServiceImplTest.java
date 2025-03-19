package Core.Domain.Services;

import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.CourierAggregate.CourierStatus;
import Core.Domain.Model.OrderAggregate.Order;
import Core.Domain.SharedKernel.Location;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DispatcherServiceImplTest {
  UUID orderID = UUID.randomUUID();
  Location deliverTo = new Location(5, 5 );
  Order order = new Order(orderID, deliverTo);
  DispatchServiceImpl dispatcherService = new DispatchServiceImpl();


  @Test
  public void checkDispatcher1(){


    ArrayList<Courier> couriers = new ArrayList<Courier>();
    Courier courier = new Courier("Vasily", "bike", 2, new Location(1,1));
    couriers.add(courier);
    dispatcherService.dispatch(order, couriers);

    assertEquals(courier.getCourierStatus(), CourierStatus.BUSY);
    assertEquals(courier.calculateTimeToLocation(deliverTo), 4);

    Courier courier2 = new Courier("Vasily2", "bike", 2, new Location(3,3));
    couriers.add(courier2);
    assertEquals(courier2.getCourierStatus(), CourierStatus.BUSY);
    assertEquals(courier.calculateTimeToLocation(deliverTo), 2);
  }

  @Test
  public void checkDispatcher2(){
    ArrayList<Courier> couriers = new ArrayList<Courier>();
    Courier courier = new Courier("Vasily", "bike", 2, new Location(1,1));
    Courier courier2 = new Courier("Vasily2", "bike", 2, new Location(3,3));
    Courier courier3 = new Courier("Vasily3", "hike", 1, new Location(10,10));

    couriers.add(courier);
    couriers.add(courier2);
    couriers.add(courier3);

    dispatcherService.dispatch(order, couriers);

    assertNotEquals(courier.getCourierStatus(), CourierStatus.BUSY);
    assertNotEquals(courier3.getCourierStatus(), CourierStatus.BUSY);
    assertEquals(courier2.getCourierStatus(), CourierStatus.BUSY);

  }



}
