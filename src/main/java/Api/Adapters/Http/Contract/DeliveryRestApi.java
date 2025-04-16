package Api.Adapters.Http.Contract;

import com.wordnik.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.json.JSONObject;
import org.openapitools.client.JSON;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Singleton
@Api(value = "/delivery", description = "Operations about delivery")
public class DeliveryRestApi{

  @GET
  @Path("/kek")
  //@Produces(MediaType.APPLICATION_JSON)
  @Operation(
          summary = "Test endpoint",
          description = "Returns a test response",
          operationId = "getKek",
          tags = { "delivery" }
  )
  public Response getkek(){
    JSONObject jo = new JSONObject();
    jo.put("name", "jon doe");
    jo.put("age", "22");
    jo.put("city", "chicago");
    return Response.ok(jo.toString()).build();
  }


  @GET
  @Path("/createOrder")
  @Operation(
          summary = "Create order",
          description = "creates a random order"
  )
  public Response createOrder(){


    return Response.ok().entity(  new JSON()).build();
  }

  @GET
  @Path("/getCouriers")
  @Operation(
          summary = "Get couriers",
          description = "Returns a list of couriers"
  )
  public Response getCouriers() throws NotFoundException {
    // do some magic!
    return Response.ok().entity(  "getCouriers!").build();
  }

  @GET
  @Path("/getOrders")
  @Operation(
          summary = "Get all orders",
          description = "Returns a list of orders"
  )
  public Response getOrders() throws NotFoundException {
    // do some magic!
    return Response.ok().entity(  "getOrders!").build();
  }
}
