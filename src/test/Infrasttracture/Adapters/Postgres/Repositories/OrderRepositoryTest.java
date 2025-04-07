package Infrasttracture.Adapters.Postgres.Repositories;

import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.CourierAggregate.Transport;
import Core.Domain.Model.OrderAggregate.Order;
import Core.Domain.Model.OrderAggregate.OrderStatus;
import Core.Domain.SharedKernel.Location;
import Infrastructure.Adapters.Postgres.Repositories.CourierRepository;
import Infrastructure.Adapters.Postgres.Repositories.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

public class OrderRepositoryTest {
  Transport car = new Transport("Car", 3);
  Transport bike = new Transport("bike", 2);
  Transport person = new Transport("person", 1);
  OrderRepository orderRep = OrderRepository.getRepository();
  CourierRepository courierRep = CourierRepository.getRepository();

  @Test
  public void testAddOrder(){
    orderRep.addOrder(UUID.randomUUID(), new Location(10,10));
  }

  @Test
  public void tryAssignCourierToOrder(){
    Courier courier = courierRep.getCourier(UUID.fromString("35471048-d085-8543-4f49-03d0b564c285"));
    boolean isUpdated = orderRep.updateOrder(UUID.fromString("4be4220d-a2d0-43ac-8147-10786288aa2c"), order -> {order.assignCourier(courier);
      order.completeOrder();});
    System.out.println(isUpdated);
  }

  @Test
  public void tryUpdateOrderWithRandomCourier(){
    Courier courier = courierRep.getCourier(UUID.fromString("642be068-2406-575a-2057-0912dcf47001"));
    Courier courier1 = new Courier("test", car, new Location(1,1));
    boolean isUpdated = orderRep.updateOrder(UUID.fromString("f244fd5a-06fb-4eee-a167-9d42082879c3"), order -> {order.assignCourier(courier1);
    });
    System.out.println(isUpdated);
  }

  @Test
  public void tryGetOrder(){
    Order order = orderRep.getOderById(UUID.fromString("4be4220d-a2d0-43ac-8147-10786288aa2c"));
    System.out.println(order.getDeliveryLocation());
    Assertions.assertEquals(order.getUUID(),UUID.fromString("4be4220d-a2d0-43ac-8147-10786288aa2c"));
  }

  @Test
  public void tryGetCreatedOrders(){
    List order = orderRep.getOrdersByStatus(OrderStatus.CREATED);
    System.out.println(order);
  }

}
