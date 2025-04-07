package Core.Domain.SharedKernel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import static java.lang.Math.abs;

@Embeddable
public class Location {

  @Column(name = "loc_x")
  private int xCoordinate;

  @Column(name = "loc_y")
  private int yCoordinate;

  private static final int MINX = 0;
  private static final int MINY = 0;
  private static final int MAXX = 10;
  private static final int MAXY = 10;

  private Location(){}

  public int getxCoordinate() {
    return xCoordinate;
  }

  public int getyCoordinate() {
    return yCoordinate;
  }

  public Location(int x, int y){
    this.xCoordinate = x > MINX&&x<=MAXX? x : geIncorrectCoordinateException(x, y);
    this.yCoordinate = y > MINY&&y<=MAXY? y : geIncorrectCoordinateException(x, y);
  }

  public int geIncorrectCoordinateException(int x, int y){
    throw new RuntimeException("incorrect coordinate "+x+":"+y);
  }

  public int distanceTo(Location other){
    int otherX = other.getxCoordinate();
    int otherY = other.getyCoordinate();
    return abs(otherX-xCoordinate)+abs(otherY-yCoordinate);
  }

  public static Location getRandomLocation(){
    return new Location(((int) (Math.random()*10))+1,(int) (Math.random() * 10)+1);
  }

  @Override
  public String toString(){
    return "("+xCoordinate+";"+yCoordinate+")";
  }

  @Override
  public boolean equals(Object other){
    if (other == null) {
      return false;
    }

    if (other.getClass() != this.getClass()) {
      return false;
    }

    final Location otherLoc = (Location) other;

    if ((this.xCoordinate != (otherLoc).xCoordinate)) {
      return false;
    }

    if ((this.yCoordinate != (otherLoc).yCoordinate)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Integer.parseInt(String.valueOf(this.xCoordinate) + String.valueOf(this.yCoordinate));
  }
}
