package hy.api.core.estate.unit;

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

  public UnitSummary(String unitNumber, int floorNumber, UnitStatus status) {
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

}
