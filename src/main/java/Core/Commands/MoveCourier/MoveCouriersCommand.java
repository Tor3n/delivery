package Core.Commands.MoveCourier;

import Core.Application.KafkaNotifications;
import Core.Commands.Commandable;
import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.CourierAggregate.Transport;
import Core.Domain.Model.OrderAggregate.Order;
import Core.Domain.Model.OrderAggregate.OrderStatus;
import Core.Domain.SharedKernel.Location;
import Infrastructure.Adapters.Postgres.Repositories.OrderRepository;
import java.util.List;

public class MoveCouriersCommand implements Commandable {
  OrderRepository orderRep = OrderRepository.getRepository();

  @Override
  public boolean command() {
    List<Order> assignedOrders = orderRep.getAssignedOrders();
    for(Order order : assignedOrders){
      orderRep.updateOrder(order.getUUID(), order1 -> {
        Courier orderCourier = order1.getAssignedCourier();
        Transport currTransport = orderCourier.getTransport();

        Location newLoc = currTransport.move(orderCourier.getCurrentLocation(), order.getDeliveryLocation());
        orderCourier.setCurrentLocation(newLoc);
        if(newLoc.equals(order1.getDeliveryLocation())){
          order1.completeOrder();
          KafkaNotifications notifications = new KafkaNotifications();
          notifications.notifyDomainChange(order.getUUID(), OrderStatus.COMPLETED);
          orderCourier.setFree();
        }
      });
    }
    return true;
  }


}
