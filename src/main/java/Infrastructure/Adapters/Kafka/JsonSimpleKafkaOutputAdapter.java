package Infrastructure.Adapters.Kafka;

import Core.Configuration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class JsonSimpleKafkaOutputAdapter implements KafkaOutPort{
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSimpleKafkaOutputAdapter.class);
    private static Configuration conf = Configuration.load();

    //TEST
    public static void main(String[] args) {
        JsonSimpleKafkaOutputAdapter adapter = new JsonSimpleKafkaOutputAdapter();
        adapter.publish("orderChanged", "value2");
    }

    @Override
    public boolean publish(String key, String value) {
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        String topic = "order.status.changed";
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

        LOGGER.info("Producer started. Waiting for messages");

        try{
            RecordMetadata metadata = producer.send(record).get();
            System.out.printf("Message sent successfully: topic = %s, partition = %d, offset = %d%n",
                    metadata.topic(), metadata.partition(), metadata.offset());
            //Thread.sleep(500);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }  catch (RuntimeException e) {
            e.printStackTrace();
            LOGGER.error(" Could not process JSON");
            return false;
        } finally {
            producer.close();
        }

        return false;
    }

}
