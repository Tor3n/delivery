package Api.Adapters.Kafka;

public interface KafkaPort<Responce> {
    public Responce get();

    public boolean set();
}
