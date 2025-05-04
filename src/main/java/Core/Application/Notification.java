package Core.Application;

import Core.Domain.Model.OrderAggregate.OrderStatus;

import java.util.UUID;

public interface Notification {
    boolean notifyDomainChange(UUID id, OrderStatus status);
}
