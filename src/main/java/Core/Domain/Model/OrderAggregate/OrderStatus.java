package Core.Domain.Model.OrderAggregate;

import java.util.stream.Stream;

public enum OrderStatus {

  CREATED(0, "Created"),
  ASSIGNED(1, "Assigned"),
  COMPLETED(2, "Completed");

  private final int id;
  private final String name;

  OrderStatus(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public static boolean isAssigned(OrderStatus status) {
    return ASSIGNED.equals(status);
  }

  private int getId() {
    return id;
  }

  /**
   * Retrieving an enum element by ID.
   */
  public static OrderStatus of(Integer id) {
    return Stream.of(values())
            .filter(t -> t.getId() == id)
            .findAny()
            .orElse(null);
  }
}
