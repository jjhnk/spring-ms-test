package hy.api.core.estate.lease;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class LeaseDetail {
  private Instant startedAt;
  private Instant endedAt;
  private double rentAmount;
  private double securityDeposit;
  private LeaseStatus status;

  public LeaseDetail() {}

  public LeaseDetail(
    Instant startInstant,
    Instant endInstant,
    double rentAmount,
    double securityDeposit,
    LeaseStatus status) {
    this.startedAt = startInstant;
    this.endedAt = endInstant;
    this.rentAmount = rentAmount;
    this.securityDeposit = securityDeposit;
    this.status = status;
  }

  public Instant getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(Instant startInstant) {
    this.startedAt = startInstant;
  }

  public Instant getEndedAt() {
    return endedAt;
  }

  public void setEndedAt(Instant endInstant) {
    this.endedAt = endInstant;
  }

  public double getRentAmount() {
    return rentAmount;
  }

  public void setRentAmount(double rentAmount) {
    this.rentAmount = rentAmount;
  }

  public double getSecurityDeposit() {
    return securityDeposit;
  }

  public void setSecurityDeposit(double securityDeposit) {
    this.securityDeposit = securityDeposit;
  }

  public LeaseStatus getStatus() {
    return status;
  }

  public void setStatus(LeaseStatus status) {
    this.status = status;
  }

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
    LeaseDetail other = (LeaseDetail) obj;
    return startedAt.truncatedTo(ChronoUnit.MILLIS).equals(other.startedAt.truncatedTo(ChronoUnit.MILLIS))
      && endedAt.truncatedTo(ChronoUnit.MILLIS).equals(other.endedAt.truncatedTo(ChronoUnit.MILLIS))
      && rentAmount == other.rentAmount
      && securityDeposit == other.securityDeposit
      && status.equals(other.status);
  }
  // @formatter:on


  // @formatter:off
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\"leaseDetail\":{");
    sb.append("\"startedAt\":\"" + startedAt + "\"");
    sb.append(",\"endedAt\":\"" + endedAt + "\"");
    sb.append(",\"rentAmount\":" + rentAmount + "");
    sb.append(",\"securityDeposit\":" + securityDeposit + "");
    sb.append(",\"status\":\"" + status + "\"");
    sb.append('}');
    return sb.toString();
  }
  // @formatter:on
}
