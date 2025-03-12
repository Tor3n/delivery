package Core.Domain.Model.CourierAggregate;

import Core.Domain.SharedKernel.Location;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransportTest {
  Transport car = new Transport("Car", 3);
  Transport bike = new Transport("bike", 2);
  Transport person = new Transport("person", 1);

  @Test
  public void testConstructor(){
    assertThrows(RuntimeException.class, ()-> new Transport("bike", 33));
    assertThrows(RuntimeException.class, ()-> new Transport("bike", -1));
    assertThrows(RuntimeException.class, ()-> new Transport("bike", 0));
  }

  @Test
  public void testHashcodes(){
    SecureRandom secureRandom = new SecureRandom();
    long randomPositiveLong = Math.abs(secureRandom.nextLong());
    UUID id = new UUID(Math.abs(secureRandom.nextLong()), Math.abs(secureRandom.nextLong()));
    assertEquals(id.hashCode(),id.hashCode());
    assertEquals(id,id);
  }


  @Test
  public void testMovement0(){
    Location from = new Location(1,1);
    Location to = new Location(1,9);

    assertEquals(bike.move(from,to),new Location(1,3));
  }

  @Test
  public void testMovement(){
    Location from = new Location(5,3);
    Location to = new Location(1,1);

    assertEquals(car.move(from,to),new Location(2,3));
    assertEquals(person.move(from,to),new Location(4,3));
  }

  @Test
  public void testMovement1(){
    Location from = new Location(1,1);
    Location to = new Location(5,3);

    assertEquals(car.move(from,to),new Location(4,1));
  }

  @Test
  public void testMovement3(){
    Location from = new Location(5,3);
    Location to = new Location(1,1);

    assertEquals(person.move(from,to),new Location(4,3));
  }

  @Test
  public void testMovement4(){
    Location from = new Location(1,1);
    Location to = new Location(5,1);

    assertEquals(car.move(from,to),new Location(4,1));
  }

  @Test
  public void testMovement5(){
    Location from = new Location(5,1);
    Location to = new Location(1,1);

    assertEquals(person.move(from,to),new Location(4,1));
  }

  @Test
  public void testMovement6(){
    Location from = new Location(1,1);
    Location to = new Location(1,5);

    assertEquals(car.move(from,to),new Location(1,4));
    assertEquals(person.move(from,to),new Location(1,2));
  }

  @Test
  public void testMovement7(){
    Location from = new Location(1,5);
    Location to = new Location(1,1);

    assertEquals(car.move(from,to),new Location(1,2));
    assertEquals(person.move(from,to),new Location(1,4));
  }

}
