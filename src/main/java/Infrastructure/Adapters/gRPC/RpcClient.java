package Infrastructure.Adapters.gRPC;

import Core.Configuration;
import Core.Domain.SharedKernel.Location;
import geo.GeoGrpc;
import geo.Rpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.*;


public class RpcClient implements GeoPort {
  private ManagedChannel channel;
  private static Configuration conf = Configuration.load();

  public RpcClient(String host, int port) {
    this.channel = ManagedChannelBuilder.forAddress(host,port).usePlaintext().build();
  }

  @Override
  public Location callService(String streetName) {

    Rpc.GetGeolocationRequest request = Rpc.GetGeolocationRequest.newBuilder()
            .setStreet(streetName)
            .build();

    GeoGrpc.GeoBlockingStub stub = GeoGrpc.newBlockingStub(channel);
    Rpc.GetGeolocationReply response = stub.getGeolocation(request);

    geo.Rpc.Location loc = response.getLocation();

    return new Location(loc.getX(), loc.getY());
  }

  public Location callWithCountdown(String streetName){
    Location oo = (Location) executeCall(()-> callService(streetName));
    return oo;
  }

  public Object executeCall(Callable task){
    ExecutorService service = Executors.newSingleThreadExecutor();
    Future future = service.submit(task);

    try{
      return future.get(3, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      future.cancel(true);
    } catch (ExecutionException | TimeoutException e) {
      e.printStackTrace();
    } finally {
      service.shutdown();
    }

    return null;
  }

  public void closeChannel(){
    if(channel!=null){
      this.channel.shutdown();
    }
  }


}
