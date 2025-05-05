package Infrastructure.Adapters.Postgres.Entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox", schema = "delivery")
public class OutboxMessage {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-generator")
  @SequenceGenerator(
          name = "sequence-generator",
          sequenceName = "outboxmessage_seq",
          allocationSize = 1
  )
  private long id;

  @Column(name = "type", nullable = false)
  private String type;

  @Column(name = "message", nullable = false)
  private String message;

  @Column(name = "creDate", nullable = false)
  private Instant creDate;

  protected OutboxMessage(){}

  public OutboxMessage( String type, String message, Instant creDate) {
    this.type = type;
    this.message = message;
    this.creDate = creDate;
  }

  public String getMessage() {
    return message;
  }
}
