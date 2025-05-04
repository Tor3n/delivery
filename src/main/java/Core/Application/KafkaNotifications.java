package Core.Application;

import Core.Domain.Model.OrderAggregate.OrderStatus;
import Infrastructure.Adapters.Kafka.JsonSimpleKafkaOutputAdapter;
import org.json.JSONObject;

import java.util.UUID;

public class KafkaNotifications implements Notification {

    public static void main(String[] args) {
        OrderStatus status = OrderStatus.ASSIGNED;
        System.out.println(status);
    }

    public boolean notifyDomainChange(UUID id, OrderStatus status){
        DomainEventHandler handler = new DomainEventHandler();
        handler.addDomainEvents( () -> {
            JsonSimpleKafkaOutputAdapter adapter = new JsonSimpleKafkaOutputAdapter();
            JSONObject object = new JSONObject();
            object.append("orderId", id);
            object.append("orderStatus", status);

            adapter.publish("orderChanged", object.toString());
        });
        handler.notifyListeners();
        return true;
    }

}

