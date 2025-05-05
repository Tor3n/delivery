package Infrastructure.Adapters.Postgres.Repositories;


import Core.Domain.Model.OrderAggregate.Order;
import Core.Domain.Model.OrderAggregate.OrderStatus;
import Infrastructure.Adapters.Postgres.Entity.OutboxMessage;
import Infrastructure.Adapters.Postgres.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OuboxRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(OuboxRepository.class);

  private static OuboxRepository repository = new OuboxRepository();
  public static OuboxRepository getRepository(){ return repository; }


  public void persistMessage(String type, String message) {
    Session session = HibernateUtil.getCurrentSF().openSession();
    Transaction tr = null;
    try{

      OutboxMessage outMessage = new OutboxMessage(type, message, Instant.now());
      tr = session.beginTransaction();

      session.persist(outMessage);

      tr.commit();
    } catch (Exception e){
      if (tr!=null) tr.rollback();
      LOGGER.error(e.getMessage());
    } finally {
      session.close();
    }
  }

  public List<OutboxMessage> getMessages() {
    Session session = HibernateUtil.getCurrentSF().openSession();
    try{
      String HQL = "FROM OutboxMessage WHERE 1 = 1  ";
      Query<OutboxMessage> query = session.createQuery(HQL, OutboxMessage.class);

      return query.getResultList();
    } catch (Exception e){
      LOGGER.error(e.getMessage());
    } finally {
      session.close();
    }
    return null;
  }

  public void deleteMessages(List<OutboxMessage> messages) {
    if (messages == null || messages.isEmpty()) {
      return;
    }

    Session session = HibernateUtil.getCurrentSF().openSession();
    Transaction transaction = null;

    try {
      transaction = session.beginTransaction();

      int batchSize = 50;
      int i = 0;

      for (OutboxMessage message : messages) {
        session.remove(message);

        // Flush and clear session periodically to manage memory
        if (i % batchSize == 0 && i > 0) {
          session.flush();
          session.clear();
        }
        i++;
      }

      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      LOGGER.error("Failed to delete messages: " + e.getMessage());
      throw new RuntimeException("Failed to delete messages", e);
    } finally {
      session.close();
    }
  }


}
