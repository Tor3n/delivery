package Core.Application;

import Core.Domain.Model.OrderAggregate.OrderStatus;
import Infrastructure.Adapters.Kafka.JsonSimpleKafkaOutputAdapter;
import org.json.JSONObject;

import java.util.UUID;

public class KafkaNotifications implements Notification {

    public boolean notifyDomainChange(UUID id, OrderStatus status){
        DomainEventHandler handler = new DomainEventHandler();

        handler.addDomainEvents( () -> {
            JsonSimpleKafkaOutputAdapter adapter = new JsonSimpleKafkaOutputAdapter();
            JSONObject object = new JSONObject();
            object.append("orderId", id);
            object.append("orderStatus", status);

            return adapter.publish("orderChanged", object.toString());
        });

        return handler.notifyListeners();
    }

}

