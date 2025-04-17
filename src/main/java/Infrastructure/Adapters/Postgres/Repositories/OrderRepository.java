package Infrastructure.Adapters.Postgres.Repositories;

import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.OrderAggregate.Order;
import Core.Domain.Model.OrderAggregate.OrderStatus;
import Core.Domain.SharedKernel.Location;
import Core.Ports.OrderRepositoryInterface;
import Infrastructure.Adapters.Postgres.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class OrderRepository implements OrderRepositoryInterface {
  private static final Logger LOGGER = LoggerFactory.getLogger(OrderRepository.class);

  private static OrderRepository repository = new OrderRepository();
  public static OrderRepository getRepository(){ return repository; }

  @Override
  public boolean addOrder(UUID id, Location deliverToLocation) {
    Session session = HibernateUtil.getCurrentSF().openSession();
    Transaction tr = null;
    try{
      Order order = new Order(id, deliverToLocation);
      tr = session.beginTransaction();
      session.persist(order);
      tr.commit();
      return true;
    } catch (Exception e){
      if (tr!=null) tr.rollback();
      LOGGER.error(e.getMessage());
      return false;
    } finally {
      session.close();
    }
  }

  @Override
  public boolean updateOrder(UUID id, OrderUpdater updater) {
    Session session = HibernateUtil.getCurrentSF().openSession();
    Transaction tr = null;
    try{
      tr = session.beginTransaction();
      Order order = session.get(Order.class, id);

      if (order!=null){
        updater.update(order);
        tr.commit();
      } else return false;

      return true;
    } catch (Exception e){
      if (tr!=null) tr.rollback();
      LOGGER.error(e.getMessage());
      return false;
    } finally {
      session.close();
    }
  }

  @Override
  public Order getOderById(UUID id) {
    Session session = HibernateUtil.getCurrentSF().openSession();
    try{
      return session.get(Order.class, id);
    } catch (Exception e){
      LOGGER.error(e.getMessage());
      session.close();
    }
    return null;
  }

  @Override
  public List<Order> getOrdersByStatus(OrderStatus status) {
    Session session = HibernateUtil.getCurrentSF().openSession();
    try{
      String HQL = "FROM Order WHERE 1 = 1 and orderStatus = :OrderStatus";
      Query<Order> query = session.createQuery(HQL, Order.class);
      query.setParameter("OrderStatus", status);

      return query.getResultList();
    } catch (Exception e){
      LOGGER.error(e.getMessage());
    } finally {
      session.close();
    }
    return null;
  }

  @Override
  public Order draftOneCreatedOrder() {
    List<Order> orders = getOrdersByStatus(OrderStatus.CREATED);
    return orders.size()<1 ? null : orders.get(0);
  }

  @Override
  public List<Order> getAssignedOrders() {
    return getOrdersByStatus(OrderStatus.ASSIGNED);
  }

  @FunctionalInterface
  public interface OrderUpdater{
    void update(Order order);
  }
}
