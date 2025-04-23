package Infrasttracture.Adapters.gRPC;

import Core.Configuration;
import Core.Domain.SharedKernel.Location;
import Infrastructure.Adapters.gRPC.RpcClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RpcClientTest {
  private static Configuration conf = Configuration.load();

  @Test
  public void testRpcClient(){
    RpcClient client = new RpcClient(conf.getGeoName(), conf.getGeoPort());
    //TODO add testContainers (curently needs a container)
    try {
      Location loc = client.callService("Тестировочная");
      assertEquals(new Location(1,1), loc);
    } finally {
      client.closeChannel();
    }
  }

  @Test
  public void testRpcClientWithDelay(){
    RpcClient client = new RpcClient(conf.getGeoName(), conf.getGeoPort());
    //TODO add testContainers (curently needs a container)
    try {
       Location loc = client.callWithCountdown("Тестировочная");
       assertEquals(new Location(1,1), loc);
    } finally {
      client.closeChannel();
    }
  }
}
