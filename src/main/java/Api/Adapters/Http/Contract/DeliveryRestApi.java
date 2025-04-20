package Api.Adapters.Http.Contract;

import Core.Commands.CreateOrder.CreateOrderCommand;
import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.SharedKernel.Location;
import Core.Queries.GetAllCouriersQuery;
import Core.Queries.GetBusyCouriersQuery;
import Core.Queries.GetUnfinishedOrdersQuery;
import com.wordnik.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.json.JSONObject;
import org.openapitools.client.JSON;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

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
          description = "creates an order"
  )
  public Response createOrder(@QueryParam("uuid") String uuid,
                              @QueryParam("locX") int locX,
                              @QueryParam("locY") int locY){
    CreateOrderCommand c = new CreateOrderCommand(UUID.fromString(uuid), new Location(locX,locY));
    c.command();
    return Response.ok().entity("ok").build();
  }

  @GET
  @Path("/getCouriers")
  @Operation(
          summary = "Get couriers",
          description = "Returns a list of couriers"
  )
  public Response getCouriers() throws NotFoundException {
    GetAllCouriersQuery couriers = new GetAllCouriersQuery();
    List<Courier> list = couriers.query();
    JSONObject jo = new JSONObject();
    for (int i = 0; i < list.size(); i++) {
      jo.put("Courier N"+i, list.get(i));
    }
    return Response.ok().entity(jo.toString()).build();
  }

  @GET
  @Path("/getOrders")
  @Operation(
          summary = "Get all orders",
          description = "Returns a list of orders"
  )
  public Response getOrders() throws NotFoundException {
    GetUnfinishedOrdersQuery query = new GetUnfinishedOrdersQuery();
    List<Courier> list = query.query();
    JSONObject jo = new JSONObject();
    for (int i = 0; i < list.size(); i++) {
      jo.put("Order N"+i, list.get(i));
    }
    return Response.ok().entity(jo.toString()).build();
  }
}
