package Core.Domain.Model.OrderAggregate;

import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.CourierAggregate.Transport;
import Core.Domain.SharedKernel.Location;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Order {

  private UUID id;
  private Location deliveryLocation;
  private OrderStatus deliveryStatus;
  private Courier assignedCourier;
  private static final ConcurrentHashMap<UUID, Order> orderCache = new ConcurrentHashMap<UUID, Order>();

  private Order(){}

  public Order(UUID id, Location deliverToLocation){
    this.id = id;
    this.deliveryLocation = deliverToLocation;
    deliveryStatus = OrderStatus.CREATED;
  }

  public boolean assignCourier(Courier courier){
    this.assignedCourier = courier;
    deliveryStatus = OrderStatus.ASSIGNED;
    orderCache.putIfAbsent(this.id, this);
    return true;
  }

  public boolean completeOrder(){
    deliveryStatus = OrderStatus.COMPLETED;
    Order completedOrder = orderCache.computeIfPresent(this.id, (k, v) -> {v.moveOrderToCompleted();
      return v;
    });
    if(completedOrder != null){
      return true;
    }
    return false;
  }

  private boolean moveOrderToCompleted(){
    deliveryStatus = OrderStatus.COMPLETED;
    return true;
  }

  public UUID getUUID(){
    return id;
  }

  @Override
  public boolean equals(Object other){
    if (other == null) {
      return false;
    }

    if (other.getClass() != this.getClass()) {
      return false;
    }

    return ((Transport) other).getUUID().equals(this.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
