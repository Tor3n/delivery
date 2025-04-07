package Api.Adapters.Http.restAPI;

import Api.Adapters.Http.restAPI.annotation.DeliveryApi;
import Core.Commands.CreateOrder.CreateOrderCommand;
import Core.Domain.SharedKernel.Location;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;


@Path("/")
@Singleton
public class DeliveryRestApi {

    @GET
    @Path("version")
    @DeliveryApi
    public Response getVersion() {
        Map<String, String> versionInfo = new HashMap<>();
        versionInfo.put("version", "0.1.0");
        return new JsonResponse<>(Response.Status.OK, " version", versionInfo).build();
    }


}
