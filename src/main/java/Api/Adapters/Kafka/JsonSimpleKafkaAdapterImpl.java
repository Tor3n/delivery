package Api.Adapters.Kafka;

import Core.Commands.Commandable;
import Core.Configuration;
import Core.Domain.SharedKernel.Location;
import Infrastructure.Adapters.Postgres.Repositories.OrderRepository;
import Infrastructure.Adapters.gRPC.RpcClient;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.*;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;


public class JsonSimpleKafkaAdapterImpl implements KafkaPort, Commandable {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSimpleKafkaAdapterImpl.class);
    private static Configuration conf = Configuration.load();

    //TEST
    public static void main(String[] args) {
        JsonSimpleKafkaAdapterImpl impl = new JsonSimpleKafkaAdapterImpl();
        impl.poll();
    }

    public boolean poll() {
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test"); // Consumer group ID
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest"); // Start reading from the beginning of the topic
        props.put("member.id","testmember");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        String topic = "basket.confirmed";
        consumer.subscribe(Collections.singletonList(topic));

        LOGGER.info("check partition assignment");

        consumer.subscribe(Collections.singletonList(topic), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.println("Partitions revoked: " + partitions);
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.println("Partitions assigned: " + partitions);
            }
        });

        LOGGER.info("Consumer started. Waiting for messages");

        try{
            //poll new messages
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));

            if(records.isEmpty()) LOGGER.info("no records...");

            for(ConsumerRecord<String, String> record : records){
                LOGGER.info("Got message: "+record.key()+" "+record.value()+" "+record.partition()+" "+record.offset());

                String jsonMessage = record.value();
                processJSONMessage(jsonMessage);
            }
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }  catch (RuntimeException e) {
            e.printStackTrace();
            LOGGER.error(" Could not process JSON");
            return false;
        } finally {
            consumer.close();
        }

        return true;
    }

    private static void processJSONMessage(String jsonMessage) throws RuntimeException{
        JSONObject obj = new JSONObject(jsonMessage);
        String basketId = obj.getString("basketId");
        String streetName = obj.getJSONObject("address").getString("street");
        LOGGER.info("basketID is: "+basketId);
        LOGGER.info("the street is: "+streetName);
        RpcClient client = new RpcClient(conf.getGeoName(), conf.getGeoPort());
        Location loc = client.callService(streetName);
        OrderRepository orderRep = OrderRepository.getRepository();
        orderRep.addOrder(UUID.fromString(basketId), loc);
    }

    @Override
    public boolean command() {
        JsonSimpleKafkaAdapterImpl impl = new JsonSimpleKafkaAdapterImpl();
        return impl.poll();
    }
}
