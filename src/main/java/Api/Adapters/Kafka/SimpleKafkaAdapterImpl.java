package Api.Adapters.Kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


public class SimpleKafkaAdapterImpl implements KafkaPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleKafkaAdapterImpl.class);

    @Inject
    public SimpleKafkaAdapterImpl(){

    }

    @Override
    public Object get() {
        return null;
    }

    @Override
    public boolean set() {
        return false;
    }


}
