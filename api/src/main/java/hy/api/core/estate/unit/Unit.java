package hy.api.core.estate.unit;

import java.util.Set;

import hy.api.core.estate.buildings.Building;
import hy.api.core.estate.lease.Lease;

public class Unit {
  private int id;
  private Building building;
  private Set<Lease> leases;
  private UnitSummary summary;
  private UnitDetail detail;

  public Unit() {
    this(0, null, null, null, null);
  }

  public Unit(int id) {
    this(id, null, null, null, null);
  }

  public Unit(int id, Building building, Set<Lease> leases, UnitSummary summary, UnitDetail detail) {
    this.id = id;
    this.building = building;
    this.leases = leases;
    this.summary = summary;
    this.detail = detail;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Building getBuilding() {
    return building;
  }

  public void setBuilding(Building building) {
    this.building = building;
  }

  public Set<Lease> getLeases() {
    return leases;
  }

  public void setLeases(Set<Lease> leases) {
    this.leases = leases;
  }

  public UnitSummary getSummary() {
    return summary;
  }

  public void setSummary(UnitSummary unitSummary) {
    this.summary = unitSummary;
  }

  public UnitDetail getDetail() {
    return detail;
  }

  public void setDetail(UnitDetail unitDetails) {
    this.detail = unitDetails;
  }

}
