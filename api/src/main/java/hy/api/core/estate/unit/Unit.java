package hy.api.core.estate.unit;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import hy.api.core.estate.buildings.Building;
import hy.api.core.estate.lease.Lease;

@JsonInclude(Include.NON_NULL)
public class Unit {
  private int id;
  // @JsonBackReference(@JsonBackReference(value = "building-unit"))
  private Building building;
  // @JsonManagedReference(@JsonManagedReference(value = "unit-lease"))
  @JsonInclude(Include.NON_EMPTY)
  private Set<Lease> leases = new LinkedHashSet<>();
  private UnitSummary summary;
  private UnitDetail detail;

  public Unit() {}

  public Unit(int id, UnitSummary summary, UnitDetail detail) {
    this.id = id;
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
    if (leases == null) {
      leases = new LinkedHashSet<>();
    }

    this.leases.clear();
    this.leases.addAll(leases);
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

  public Lease getLeaseById(int id) {
    if (leases == null) {
      return null;
    }

    for (Lease lease : leases) {
      if (lease.getId() == id) {
        return lease;
      }
    }
    return null;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, summary, detail);
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

    Unit other = (Unit) obj;
    return id == other.id
      && summary.equals(other.summary)
      && detail.equals(other.detail);
  }
  // @formatter:on

  @Override
  // @formatter:off
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\"unit\":{");
    sb.append("\"id\":" + id);
    if (building != null) {
      sb.append(",\"building\":" + building.toReferredString());
    }
    sb.append(",\"leases\": [");
    Iterator<Lease> iterator = leases.iterator();
    while (iterator.hasNext()) {
      Lease lease = iterator.next();
      sb.append(lease.toReferedString());
      if (iterator.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append("]");

    if (summary != null) {
      sb.append(',' + summary.toString());
    }
    if (detail != null) {
      sb.append(',' + detail.toString());
    }
    sb.append('}');
    return sb.toString();
  }
  // @formatter:on

  public String toReferredString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("\"id\":" + id);
    if (summary != null) {
      sb.append("," + summary.toString());
    }
    if (detail != null) {
      sb.append("," + detail.toString());
    }
    sb.append('}');
    return sb.toString();
  }
}
