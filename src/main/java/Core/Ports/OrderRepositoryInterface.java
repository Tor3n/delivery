package Core.Ports;

import Core.Domain.Model.OrderAggregate.Order;
import Core.Domain.Model.OrderAggregate.OrderStatus;
import Core.Domain.SharedKernel.Location;
import Infrastructure.Adapters.Postgres.Repositories.CourierRepository;
import Infrastructure.Adapters.Postgres.Repositories.OrderRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepositoryInterface {

  public boolean addOrder(UUID id, Location deliverToLocation);

  public boolean updateOrder(UUID id, OrderRepository.OrderUpdater updater);

  public Order getOderById(UUID id);

  public Order draftOneCreatedOrder();

  public List<Order> getOrdersByStatus(OrderStatus status);

  public List<Order> getAssignedOrders();
}
