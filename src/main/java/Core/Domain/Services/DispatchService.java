package Core.Domain.Services;

import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.OrderAggregate.Order;

import java.util.List;

public interface DispatchService {

  public boolean dispatch(Order order, List<Courier> courires);
}
