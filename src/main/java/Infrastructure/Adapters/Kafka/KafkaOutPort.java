package Infrastructure.Adapters.Kafka;

public interface KafkaOutPort {
    public boolean publish(String  key, String value);
}
