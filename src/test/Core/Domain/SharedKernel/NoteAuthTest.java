package Core.Domain.SharedKernel;


import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NoteAuthTest {

  @Test
  public void testLocationMethods() {
    assertEquals(new Location(2,3), new Location(2,3));
    assertEquals(new Location(4,4), new Location(4,4));
    assertNotEquals(new Location(4,4),new Location(4,2));
    assertThrows(RuntimeException.class, ()-> new Location(2,-2));
    assertThrows(RuntimeException.class, ()-> new Location(2,11));
    assertThrows(RuntimeException.class, ()-> new Location(0,11));
    assertEquals(new Location(2,2).distanceTo(new Location(3,3)),2);
    assertEquals(new Location(2,2).distanceTo(new Location(3,3)),2);
  }

  @Test
  @RepeatedTest(120)
  public void testRandomLocMethod() {
    Location l = Location.getRandomLocation();
    System.out.println(l.getxCoordinate()+"y:"+l.getyCoordinate());
  }

}
