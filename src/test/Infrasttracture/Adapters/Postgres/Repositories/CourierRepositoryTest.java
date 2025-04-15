package Infrasttracture.Adapters.Postgres.Repositories;

import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.CourierAggregate.CourierStatus;
import Core.Domain.Model.CourierAggregate.Transport;
import Core.Domain.SharedKernel.Location;
import Infrastructure.Adapters.Postgres.HibernateUtil;
import Infrastructure.Adapters.Postgres.Repositories.CourierRepository;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CourierRepositoryTest {
  Transport car = new Transport("Car", 3);
  Transport bike = new Transport("bike", 2);
  Transport person = new Transport("person", 1);
  CourierRepository rep = CourierRepository.getRepository();

  @Test
  public void testAddCourier(){
    rep.addCourier("VASA33", bike, new Location(7,8));
  }

  @Test
  public void tryGetCourier(){
    Courier vasiliii = rep.getCourier(UUID.fromString("1fdfad4e-eb94-4ed6-88ee-19987aa9d84c"));
    System.out.println(vasiliii);
  }

  @Test
  public void tryChangetCourier(){
    Courier vasiliii = rep.getCourier(UUID.fromString("1fdfad4e-eb94-4ed6-88ee-19987aa9d84c"));
  }

  @Test
  public void tryRemoveCourier(){
    //assertTrue(rep.removeCourier(UUID.fromString("1fdfad4e-eb94-4ed6-88ee-19987aa9d84c")));
  }


  @Test
  public void tryMoveCourier(){
    boolean isUpdated = rep.updateCourier(UUID.fromString("658068ed-5912-f390-46e6-f07fb205c681"), courier -> {
      courier.setCurrentLocation(new Location(2,2));});
    System.out.println(isUpdated);

  }

  @Test
  public void tryUpdateCourier(){
    boolean isUpdated = rep.updateCourier(UUID.fromString("642be068-2406-575a-2057-0912dcf47001"), courier -> {courier.setCourierStatus(CourierStatus.BUSY);
    courier.setCurrentLocation(new Location(2,2));});
    System.out.println(isUpdated);

    Courier vasiliii = rep.getCourier(UUID.fromString("642be068-2406-575a-2057-0912dcf47001"));
    System.out.println(vasiliii);
  }

  @Test
  public void tryAssignCourier(){
    rep.updateCourier(UUID.fromString("642be068-2406-575a-2057-0912dcf47001"), courier -> {
      //courier.setBusy(order);
    });
  }



  @Test
  public void getAllBusyCouriers(){
    System.out.println(rep.getAllCouriersByStatus(CourierStatus.READY));
  }

  @Test
  public void getAllNACouriers(){
    System.out.println(rep.getAllCouriersByStatus(CourierStatus.NOT_AVAILABLE));
  }

}
