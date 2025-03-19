package Core.Domain.Services;

import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.CourierAggregate.CourierStatus;
import Core.Domain.Model.OrderAggregate.Order;
import Core.Domain.Model.OrderAggregate.OrderStatus;

import java.util.*;
import java.util.stream.IntStream;

public class DispatchServiceImpl implements DispatchService {

  @Override
  public boolean dispatch(Order order, List<Courier> courires) {
    if (order.getDeliveryStatus()!= OrderStatus.CREATED){
      throw new RuntimeException("incorrect orderStatus");
    }

    Courier bestCourier = null;
    int bestCourierTime = Integer.MAX_VALUE;

    for(Courier courier : courires){
      if (courier.getCourierStatus() == CourierStatus.READY){
        int time = courier.calculateTimeToLocation(order.getDeliveryLocation());
        if(time<bestCourierTime){
         bestCourierTime = time;
         bestCourier = courier;
        }
      }
    }

    if (bestCourier != null){
      bestCourier.setBusy(order);
    }

    return bestCourier != null;
  }


}
