package Core.Domain.Model.CourierAggregate;

import Core.Domain.Model.OrderAggregate.Order;
import Core.Domain.SharedKernel.Location;
import jakarta.persistence.*;

import java.security.SecureRandom;
import java.util.UUID;

@Entity
@Table(name = "courier", schema = "delivery")
public class Courier {

  @Id
  @Column(name = "id", columnDefinition = "UUID")
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @OneToOne
  @JoinColumn(name = "transport_id", referencedColumnName = "id")
  private Transport transport;

  @Embedded
  private Location currentLocation;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "status", nullable = false)
  private CourierStatus courierStatus;

  protected Courier(){}

  private static final SecureRandom secureRandom = new SecureRandom();

  public Courier(String name, String transportName, int transportSpeed, Location location){
    this.id = new UUID(Math.abs(secureRandom.nextLong()), Math.abs(secureRandom.nextLong()));
    this.name = name;
    this.transport = new Transport(transportName, transportSpeed);
    this.currentLocation = location;
    courierStatus = CourierStatus.READY;
  }

  public Courier(String name, Transport transport, Location location){
    this.id = new UUID(Math.abs(secureRandom.nextLong()), Math.abs(secureRandom.nextLong()));
    this.name = name;
    this.transport = transport;
    this.currentLocation = location;
    courierStatus = CourierStatus.READY;
  }

  public void setCurrentLocation(Location location) {
    this.currentLocation = location;
  }

  public void setCourierStatus(CourierStatus courierStatus) {
    this.courierStatus = courierStatus;
  }

  public CourierStatus getCourierStatus() {
    return courierStatus;
  }

  public UUID getID(){
    return id;
  }

  public String getName(){
    return name;
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
  public String toString(){
    return ""+name+" , "+id+" , "+transport+" , "+courierStatus+" , "+currentLocation.getxCoordinate()+":"+currentLocation.getyCoordinate();
  }

  @Override
  public boolean equals(Object other){
    if (other == null) {
      return false;
    }

    if (other.getClass() != this.getClass()) {
      return false;
    }

    return ((Courier) other).getID().equals(this.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
