package Infrastructure.Adapters.gRPC;

import Core.Domain.SharedKernel.Location;

public interface GeoPort {
  public Location callService(String streetName);
}
