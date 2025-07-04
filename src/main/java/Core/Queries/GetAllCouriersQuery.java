package Core.Queries;

import Core.Domain.Model.CourierAggregate.Courier;
import Infrastructure.Adapters.Postgres.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetAllCouriersQuery {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetAllCouriersQuery.class);

  public List<Courier> query(){
    Session session = HibernateUtil.getCurrentSF().openSession();
    try{
      String SQL = "SELECT * FROM delivery.courier where 1 = 1 ";
      NativeQuery query = session.createNativeQuery(SQL, Courier.class);
      return query.getResultList();
    } catch (Exception e){
      LOGGER.error(e.getMessage());
    } finally {
      session.close();
    }
    return null;
  }


}
