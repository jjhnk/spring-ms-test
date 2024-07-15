package hy.api.core.estate.unit;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Summary of a unit
 *
 * @see Unit
 * @see UnitStatus
 */
public class UnitSummary {
  private String unitNumber;
  private int floorNumber;
  private UnitStatus status;

  public UnitSummary() {
    this("", 0, null);
  }

  @JsonCreator
  public UnitSummary(
    @JsonProperty("unitNumber") String unitNumber,
    @JsonProperty("floorNumber") int floorNumber,
    @JsonProperty("status") UnitStatus status) {
    this.unitNumber = unitNumber;
    this.floorNumber = floorNumber;
    this.status = status;
  }

  public UnitStatus getStatus() {
    return status;
  }

  public void setStatus(UnitStatus status) {
    this.status = status;
  }

  public String getUnitNumber() {
    return unitNumber;
  }

  public void setUnitNumber(String unitNumber) {
    this.unitNumber = unitNumber;
  }

  public int getFloorNumber() {
    return floorNumber;
  }

  public void setFloorNumber(int floorNumber) {
    this.floorNumber = floorNumber;
  }

  @Override
  public int hashCode() {
    return Objects.hash(unitNumber, floorNumber, status);
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

    UnitSummary other = (UnitSummary) obj;
    return unitNumber.equals(other.unitNumber)
      && floorNumber == other.floorNumber
      && status.equals(other.status);
  }
  // @formatter:on

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\"unitSummary\":{");
    sb.append("\"unitNumber\":\"" + unitNumber + "\"");
    sb.append(",\"floorNumber\":" + floorNumber);
    sb.append(",\"status\":\"" + status + "\"");
    sb.append('}');
    return sb.toString();


  }
}
