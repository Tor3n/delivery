package Core.Queries;

import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.OrderAggregate.Order;
import Infrastructure.Adapters.Postgres.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetUnfinishedOrdersQuery {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetUnfinishedOrdersQuery.class);

  public List<Courier> query(){
    Session session = HibernateUtil.getCurrentSF().openSession();
    try{
      String SQL = "SELECT * FROM delivery.order where status < 2 ";
      NativeQuery query = session.createNativeQuery(SQL, Order.class);
      return query.getResultList();
    } catch (Exception e){
      LOGGER.error(e.getMessage());
    } finally {
      session.close();
    }
    return null;
  }


}
