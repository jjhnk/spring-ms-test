package hy.api.core.property.unit;

/**
 * Summary of a unit
 *
 * @see Unit
 * @see UnitStatus
 */
public class UnitSummary {
  private int unitId;
  private String unitNumber;
  private int floorNumber;
  private UnitStatus status;

  public UnitSummary() {
    this(0, "", 0, null);
  }

  public UnitSummary(int unitId, String unitNumber, int floorNumber, UnitStatus status) {
    this.unitId = unitId;
    this.unitNumber = unitNumber;
    this.floorNumber = floorNumber;
    this.status = status;
  }

  public int getUnitId() {
    return unitId;
  }

  public void setUnitId(int unitId) {
    this.unitId = unitId;
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
