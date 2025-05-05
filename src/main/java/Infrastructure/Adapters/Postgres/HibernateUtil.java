package Infrastructure.Adapters.Postgres;

import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.CourierAggregate.Transport;
import Core.Domain.Model.OrderAggregate.Order;
import Core.Domain.SharedKernel.Location;
import Infrastructure.Adapters.Postgres.Entity.OutboxMessage;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import java.util.HashMap;
import java.util.Map;

public class HibernateUtil {

  private static SessionFactory sessionFactory = instantiateSF();

  public static SessionFactory instantiateSF(){
    Map<String, Object> settings = new HashMap<>();
    settings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
    settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    settings.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/toren");
    settings.put("hibernate.connection.username", "toren");
    settings.put("hibernate.connection.password", "123456");
    settings.put("hibernate.hbm2ddl.auto", "update");
    settings.put("hibernate.current_session_context_class", "thread");
    //settings.put("hibernate.show_sql", "true");
    //ettings.put("hibernate.format_sql", "true");

    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .applySettings(settings).build();

    sessionFactory = new MetadataSources(serviceRegistry).
            addAnnotatedClass(Transport.class).
            addAnnotatedClass(Courier.class).
            addAnnotatedClass(Order.class).
            addAnnotatedClass(Location.class).
            addAnnotatedClass(OutboxMessage.class).
            buildMetadata().
            buildSessionFactory();

    return sessionFactory;
  }

  public static SessionFactory getCurrentSF() {
    return sessionFactory;
  }
}
