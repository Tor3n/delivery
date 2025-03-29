package Core.Domain.Model.CourierAggregate;

import java.util.stream.Stream;

public enum CourierStatus {

  NOT_AVAILABLE(0, "NotAvailable"),
  READY(1, "Ready"),
  BUSY(2, "Busy");

  private final int id;
  private final String name;

  CourierStatus(int id, String status) {
    this.id = id;
    this.name = status;
  }

  public static boolean isNotAvailable(CourierStatus status) {
    return NOT_AVAILABLE.equals(status);
  }

  public static boolean isReady(CourierStatus status) {
    return READY.equals(status);
  }

  public static boolean isBusy(CourierStatus status) {
    return BUSY.equals(status);
  }


  /**
   * Retrieving an enum element by ID.
   */
  public static CourierStatus of (Integer id) {
    return Stream.of(values())
            .filter(t -> t.getId() == id)
            .findAny()
            .orElse(null);
  }

  private int getId() {
    return this.id;
  }


}
