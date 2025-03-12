package Core.Domain.Model.CourierAggregate;

import Core.Domain.SharedKernel.Location;

import java.security.SecureRandom;
import java.util.UUID;

import static java.lang.Math.abs;

public class Transport {

  private UUID id;
  private String name;
  private int speed;

  private static final SecureRandom secureRandom = new SecureRandom();

  private Transport(){}

  public Transport(String name, int speed){
    this.id = new UUID(Math.abs(secureRandom.nextLong()), Math.abs(secureRandom.nextLong()));

    this.name = ("".equals(name)) ? throwIncorrectInput() : name;
    this.speed = (speed<=0 || speed>3) ?  throwIncorrectInputInt() : speed;
  }

  private String throwIncorrectInput(){
    throw new RuntimeException("incorrect input");
  }

  private int throwIncorrectInputInt(){
    throw new RuntimeException("incorrect input");
  }

  public UUID getUUID(){
    return id;
  }

  public Location move (Location from, Location to){
    int distance = to.distanceTo(from);
    if (distance <= speed){
      return to;
    } else {
      Location nextStep = new Location(from.getxCoordinate(), from.getyCoordinate());

      for(int i = 0; i < speed; i++) {
        int fx=nextStep.getxCoordinate();
        int fy=nextStep.getyCoordinate();
        int tx=to.getxCoordinate();
        int ty=to.getyCoordinate();

        int nx = fx-tx>0?(fx-1):(fx-tx==0?tx:fx+1);
        int ny = fy;
        if(fx==tx){
          ny = fy-ty>0?(fy-1):(fy-ty==0?ty:fy+1);
        }
        nextStep = new Location(nx,ny);
      }

      return nextStep;
    }

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
