package Infrastructure.Adapters.Postgres.Repositories;

import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.CourierAggregate.CourierStatus;
import Core.Domain.Model.CourierAggregate.Transport;
import Core.Domain.SharedKernel.Location;
import Core.Ports.CourierRepositoryInterface;
import Infrastructure.Adapters.Postgres.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.UUID;

public class CourierRepository implements CourierRepositoryInterface {
  private static final Logger LOGGER = LoggerFactory.getLogger(CourierRepository.class);

  private static CourierRepository repository = new CourierRepository();
  public static CourierRepository getRepository(){ return repository; }

  @Override
  public void addCourier(String name, Transport transport, Location location) {
    Session session = HibernateUtil.getCurrentSF().openSession();
    Transaction tr = null;
    try{

      Courier courier = new Courier(name, transport, location);
      tr = session.beginTransaction();

      session.persist(transport);
      session.persist(courier);
      tr.commit();
    } catch (Exception e){
      if (tr!=null) tr.rollback();
      LOGGER.error(e.getMessage());
    } finally {
      session.close();
    }
  }


  private boolean removeCourier(UUID id) {
    Session session = HibernateUtil.getCurrentSF().openSession();
    Transaction tr = null;
    try{
      tr = session.beginTransaction();
      Courier courier = getCourier(id);

      if(courier != null){
        session.remove(courier);
        tr.commit();
        return true;
      } else {
        return false;
      }
    } catch (Exception e){
      LOGGER.error(e.getMessage());
      if (tr != null){
        tr.rollback();
      }
      return false;
    } finally {
      session.close();
    }
  }


  @Override
  public boolean updateCourier(UUID id, CourierUpdater updater) {
    Session session = HibernateUtil.getCurrentSF().openSession();
    Transaction tr = null;
    try{
      tr = session.beginTransaction();
      Courier olDCourier = session.get(Courier.class, id);

      if (olDCourier!=null){
        updater.update(olDCourier);
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
  public Courier getCourier(UUID id) {
    Session session = HibernateUtil.getCurrentSF().openSession();
    try{
      return session.get(Courier.class, id);
    } catch (Exception e){
      LOGGER.error(e.getMessage());
      session.close();
    }
    return null;
  }

  @Override
  public List<Courier> getAllCouriersByStatus(CourierStatus status) {
    Session session = HibernateUtil.getCurrentSF().openSession();
    try{
      String HQL = "FROM Courier WHERE 1 = 1 and courierStatus = :CourierStatus";
      Query<Courier> query = session.createQuery(HQL, Courier.class);
      query.setParameter("CourierStatus", status);

      return query.getResultList();
    } catch (Exception e){
      LOGGER.error(e.getMessage());
    } finally {
      session.close();
    }
    return null;
  }

  @FunctionalInterface
  public interface CourierUpdater{
    void update(Courier courier);
  }
}
