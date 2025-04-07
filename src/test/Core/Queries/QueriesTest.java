package Core.Queries;

import Core.Domain.Model.CourierAggregate.Courier;
import org.junit.jupiter.api.Test;

import java.util.List;

public class QueriesTest {

  @Test
  public void getBusyCouriers(){
    GetBusyCouriersQuery couriers = new GetBusyCouriersQuery();
    List<Courier> list = couriers.query();
    System.out.println(list);
  }

  @Test
  public void getBusyUnfinishedOrders(){
    GetUnfinishedOrdersQuery query = new GetUnfinishedOrdersQuery();
    List<Courier> list = query.query();
    System.out.println(list);
  }

}
