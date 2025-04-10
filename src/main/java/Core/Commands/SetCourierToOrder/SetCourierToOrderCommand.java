package Core.Commands.SetCourierToOrder;

import Core.Commands.Commandable;
import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.CourierAggregate.CourierStatus;
import Core.Domain.Model.OrderAggregate.Order;
import Core.Domain.Services.DispatchServiceImpl;
import Infrastructure.Adapters.Postgres.Repositories.CourierRepository;
import Infrastructure.Adapters.Postgres.Repositories.OrderRepository;

import java.util.List;

public class SetCourierToOrderCommand implements Commandable {
  OrderRepository orderRep = OrderRepository.getRepository();
  CourierRepository courierRep = CourierRepository.getRepository();

  @Override
  public boolean command() {
    Order freeOrder = orderRep.draftOneCreatedOrder();

    List<Courier> freeCouriers = courierRep.getAllCouriersByStatus(CourierStatus.READY);
    int min = Integer.MAX_VALUE;
    Courier candiate = null;
    for (Courier courier : freeCouriers){
      int moves = courier.countMovesTillEnd(courier.getCurrentLocation(), freeOrder.getDeliveryLocation());
      if( moves < min){
        min = moves;
        candiate = courier;
      }
    }

    Courier finalCandiate = candiate;
    orderRep.updateOrder(freeOrder.getUUID(), order -> {
      order.assignCourier(finalCandiate);
      courierRep.updateCourier(finalCandiate.getID(), courier -> {
        courier.setBusy(order);
      });
    });

    return true;
  }


  public boolean commandWithDispatcher() {
    orderRep.updateOrder(orderRep.draftOneCreatedOrder().getUUID(), order -> {
      DispatchServiceImpl dispatchService = new DispatchServiceImpl();
      dispatchService.dispatch(order, courierRep.getAllCouriersByStatus(CourierStatus.READY));
    });

    return true;
  }
}
