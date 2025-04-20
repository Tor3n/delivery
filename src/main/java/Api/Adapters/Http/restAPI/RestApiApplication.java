package Api.Adapters.Http.restAPI;

import Api.Adapters.Http.Contract.DeliveryRestApi;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;



public class RestApiApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(Api.Adapters.Http.Contract.DeliveryRestApi.class);
        resources.add(JacksonJsonProvider.class);
        resources.add(OpenApiResource.class);
        //resources.add(DeliveryRestApi.class);

        return resources;
    }
}
