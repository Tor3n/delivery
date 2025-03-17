package Core.Domain.Model.CourierAggregate;

import Core.Domain.SharedKernel.Location;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourierTest {

  @Test
  public void courrierMoveTest(){
    Courier courier = new Courier("Vasily", "bike", 2, new Location(1,1));
    assertEquals(courier.calculateTimeToLocation(new Location(5,5)), 4);
    assertEquals(courier.calculateTimeToLocation(new Location(3,2)), 2);
    Courier courier2 = new Courier("Vasily", "bike", 2, new Location(10,10));
    assertEquals(courier2.calculateTimeToLocation(new Location(5,5)), 5);
    assertEquals(courier2.calculateTimeToLocation(new Location(10, 10)), 0);
  }


}
