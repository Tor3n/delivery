package Core.Domain.Model.CourierAggregate;

import Core.Domain.Model.OrderAggregate.Order;
import Core.Domain.SharedKernel.Location;

import java.util.UUID;

public class Courier {
  private UUID id;
  private String name;
  private Transport transport;
  private Location currentLocation;
  private CourierStatus courierStatus;

  private Courier(){}

  public Courier(String name, String transportName, int transportSpeed, Location location){
    this.name = name;
    this.transport = new Transport(transportName, transportSpeed);
    this.currentLocation = location;
    courierStatus = CourierStatus.READY;
  }

  public CourierStatus getCourierStatus() {
    return courierStatus;
  }

  public UUID getID(){
    return id;
  }

  public boolean setBusy(Order order){
    courierStatus = CourierStatus.BUSY;
    order.assignCourier(this);
    return true;
  }

  public boolean setFree(Order order){
    courierStatus = CourierStatus.READY;
    order.completeOrder();
    return true;
  }

  public int calculateTimeToLocation(Location location){
    return moveTillEnd(currentLocation, location);
  }

  public int moveTillEnd(Location from, Location to){
    int sum = 0;
    if(from.equals(to)){
      return 0;
    } else {
      sum = sum + 1 + moveTillEnd(transport.move(from, to), to);
    }
    return sum;
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
