package Core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    public String getString(ConfVars c) {
        return c.getStringValue();
    }

    public int getInt(ConfVars c) {
        return c.getIntValue();
    }

    public boolean getBoolean(ConfVars c) {
        return c.getBooleanValue();
    }

    public boolean useSsl(ConfVars c) {
        return getBoolean(c);
    }

    //TODO DELIVERY_TODO
    public String ConnectionString(){
        return "";
    }
    //TODO DELIVERY_TODO
    public String GeoServiceGrpcHost(){
        return "";
    }
    //TODO DELIVERY_TODO
    public String MessageBrokerHost(){
        return "";
    }
    //TODO DELIVERY_TODO
    public String OrderStatusChangedTopic(){
        return "";
    }
    //TODO DELIVERY_TODO
    public String BasketConfirmedTopic(){
        return "";
    }

    public int getServerPort() {
        return getInt(ConfVars.DELIVERY_SERVER_PORT);
    }

    public String getServerAddress() {
        return getString(ConfVars.DELIVERY_SERVER_ADDR);
    }

    public String getServerContextPath() {
        return getString(ConfVars.DELIVERY_SERVER_CONTEXT_PATH);
    }

    public static Configuration load() {
        return new Configuration();
    }

    public void printShortInfo() {
        LOGGER.info("Server Host: {}", getServerAddress());
        LOGGER.info("Server Port: {}", getServerPort());
        LOGGER.info("Context Path: {}", getServerContextPath());
        LOGGER.info("Zeppelin Version: {}", "0.1.0");
    }

    public String getJettyName() {
        return getString(ConfVars.DELIVERY_SERVER_JETTY_NAME);
    }

    public enum ConfVars{
        CONNECTION_STRING(""),
        GEO_SERVICE_GRPC_HOST(""),
        MESSAGE_BROKER_HOST(""),
        ORDER_STATUS_CHANGED_TOPIC(""),
        BASKET_CONFIRMED_TOPIC(""),
        DELIVERY_SERVER_JETTY_THREAD_POOL_MAX(100),
        DELIVERY_SERVER_JETTY_THREAD_POOL_MIN(8),
        DELIVERY_SERVER_JETTY_THREAD_POOL_TIMEOUT(30),
        
        DELIVERY_SERVER_JETTY_USE_SSL(false),
        DELIVERY_SERVER_PORT(8080),
        DELIVERY_SERVER_ADDR("127.0.0.1"),
        DELIVERY_SERVER_CONTEXT_PATH( "/"),
        DELIVERY_SERVER_JETTY_NAME("deliery_web_server"),
        DELIVERY_SERVER_UI_DEFAULT_DIR("delivery/UI");

        private Class<?> varClass;
        private String stringValue;
        private int intValue;
        private float floatValue;
        private boolean booleanValue;
        private long longValue;

        ConfVars(String varValue) {
          this.varClass = String.class;
          this.stringValue = varValue;
          this.intValue = -1;
          this.floatValue = -1;
          this.longValue = -1;
          this.booleanValue = false;
        }

        ConfVars(int intValue) {
            this.varClass = Integer.class;
            this.stringValue = null;
            this.intValue = intValue;
            this.floatValue = -1;
            this.longValue = -1;
            this.booleanValue = false;
        }

        ConfVars(boolean booleanValue) {
            this.varClass = Boolean.class;
            this.stringValue = null;
            this.intValue = -1;
            this.longValue = -1;
            this.floatValue = -1;
            this.booleanValue = booleanValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public long getLongValue() {
            return longValue;
        }

        public float getFloatValue() {
            return floatValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public boolean getBooleanValue() {
            return booleanValue;
        }
    }
}
