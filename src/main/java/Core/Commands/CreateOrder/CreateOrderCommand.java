package Core.Commands.CreateOrder;

import Core.Commands.Commandable;
import Core.Domain.SharedKernel.Location;
import Infrastructure.Adapters.Postgres.Repositories.OrderRepository;

import java.util.UUID;

public class CreateOrderCommand implements Commandable {
  private OrderRepository orderRep = OrderRepository.getRepository();
  private UUID orderID;
  private Location location;

  public CreateOrderCommand(UUID orderID, Location location){
    this.orderID = orderID;
    this.location = location;
  }

  @Override
  public boolean command() {
    return orderRep.addOrder(orderID, location);
  }
}
