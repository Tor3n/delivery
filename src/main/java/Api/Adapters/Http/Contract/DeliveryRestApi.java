package Api.Adapters.Http.Contract;

import Api.Adapters.Http.restAPI.annotation.DeliveryApi;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
@Singleton
@DeliveryApi
@Tag(name = "Delivery REST")
public class DeliveryRestApi{

  @GET
  @Path("/kek")
  @Operation(
          summary = "test methods",
          description = "Returns kek"
  )
  @ApiResponses(value = { @ApiResponse(code = 200, message = "ok"), @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "Pet not found") })
  public Response getkek(){
    return Response.ok("kek").build();
  }

  @GET
  @Path("/createOrder")
  @Operation(
          summary = "Create order",
          description = "creates a random order"
  )
  public Response createOrder(){
    // do some magic!
    return Response.ok().entity(  "createOrder!").build();
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
