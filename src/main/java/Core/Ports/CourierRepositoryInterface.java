package Core.Ports;

import Core.Domain.Model.CourierAggregate.Courier;
import Core.Domain.Model.CourierAggregate.CourierStatus;
import Core.Domain.Model.CourierAggregate.Transport;
import Core.Domain.SharedKernel.Location;
import Infrastructure.Adapters.Postgres.Repositories.CourierRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CourierRepositoryInterface {

  public void addCourier(String name, Transport transport, Location location);

  public boolean updateCourier(UUID id, CourierRepository.CourierUpdater updater);

  public Courier getCourier(UUID id);

  public List<Courier> getAllCouriersByStatus(CourierStatus status);

}
