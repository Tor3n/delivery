package Api.Adapters.Http.restAPI;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class RestApiApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<>();
        s.add(DeliveryRestApi.class);

        return s;
    }
}
