package Core.Domain.Model.OrderAggregate;

import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.CourierAggregate.Transport;
import Core.Domain.SharedKernel.Location;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "order", schema = "delivery")
public class Order {

  @Id
  @Column(name = "id", columnDefinition = "UUID")
  private UUID id;

  @Embedded
  private Location deliveryLocation;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "status", nullable = false)
  private OrderStatus orderStatus;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "courier_id", referencedColumnName = "id", nullable = true)
  private Courier assignedCourier;

  //@Column(name = "courier_id")
  //private UUID assignedCourierID;

  protected Order(){}

  public Order(UUID id, Location deliverToLocation){
    this.id = id;
    this.deliveryLocation = deliverToLocation;
    orderStatus = OrderStatus.CREATED;
  }

  public Location getDeliveryLocation() {
    return deliveryLocation;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public Courier getAssignedCourier() {
    return assignedCourier;
  }

  public boolean assignCourier(Courier courier){
    this.assignedCourier = courier;
    this.orderStatus = OrderStatus.ASSIGNED;
    //this.assignedCourierID = courier.getID();
    return true;
  }

  public boolean completeOrder(){
    orderStatus = OrderStatus.COMPLETED;
    assignedCourier = null;
    //assignedCourierID = null;
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
