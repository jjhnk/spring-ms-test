package hy.oltp.core.estate.lease.persistence;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import hy.api.core.estate.lease.LeaseStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LeaseDetailEntity {
  @NotNull
  private Instant startedAt;

  @NotNull
  private Instant endedAt;

  @Min(0)
  private double rentAmount;

  @Min(0)
  private double securityDeposit;

  @Enumerated(EnumType.STRING)
  @NotNull
  private LeaseStatus status;

  @Override
  public int hashCode() {
    return Objects.hash(startedAt, endedAt, rentAmount, securityDeposit, status);
  }

  // @formatter:off
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    LeaseDetailEntity other = (LeaseDetailEntity) obj;
    return startedAt.truncatedTo(ChronoUnit.MILLIS).equals(other.startedAt.truncatedTo(ChronoUnit.MILLIS))
      && endedAt.truncatedTo(ChronoUnit.MILLIS).equals(other.endedAt.truncatedTo(ChronoUnit.MILLIS))
      && rentAmount == other.rentAmount
      && securityDeposit == other.securityDeposit
      && status.equals(other.status);
  }
  // @formatter:on
}
