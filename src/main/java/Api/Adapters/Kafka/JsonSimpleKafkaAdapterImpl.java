package Api.Adapters.Kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;


public class JsonSimpleKafkaAdapterImpl implements KafkaPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSimpleKafkaAdapterImpl.class);

    public JsonSimpleKafkaAdapterImpl(){

    }

    @Override
    public Object get() {
        return null;
    }

    @Override
    public boolean set() {
        return false;
    }


    public static void main(String[] args) {
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "consumer-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        String topic = "my-topic";
        consumer.subscribe(Collections.singletonList(topic));

        LOGGER.info("Consumer started. Waiting for messages");

        try{
            //poll new messages
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String, String> record : records){
                LOGGER.info("Got message: "+record.key()+" "+record.value()+" "+record.partition()+" "+record.offset());

                String jsonMessage = record.value();
                processJSONMessage(jsonMessage);
            }

        } finally {
            consumer.close();
        }

    }

    private static void processJSONMessage(String jsonMessage){
        LOGGER.info(jsonMessage);
    }

}
