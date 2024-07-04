package hy.api.core.property.unit;

public class Unit {
  private int unitId;
  private int propertyId;
  private UnitSummary unitSummary;
  private UnitDetails unitDetails;

  public Unit() {
    this(0, 0, null, null);
  }

  public Unit(int unitId, int propertyId, UnitSummary unitSummary, UnitDetails unitDetails) {
    this.unitId = unitId;
    this.propertyId = propertyId;
    this.unitSummary = unitSummary;
    this.unitDetails = unitDetails;
  }

  public int getUnitId() {
    return unitId;
  }

  public void setUnitId(int unitId) {
    this.unitId = unitId;
  }

  public int getPropertyId() {
    return propertyId;
  }

  public void setPropertyId(int propertyId) {
    this.propertyId = propertyId;
  }

  public UnitSummary getUnitSummary() {
    return unitSummary;
  }

  public void setUnitSummary(UnitSummary unitSummary) {
    this.unitSummary = unitSummary;
  }

  public UnitDetails getUnitDetails() {
    return unitDetails;
  }

  public void setUnitDetails(UnitDetails unitDetails) {
    this.unitDetails = unitDetails;
  }
}
