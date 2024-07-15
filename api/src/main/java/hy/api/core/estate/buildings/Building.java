package hy.api.core.estate.buildings;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import hy.api.core.estate.unit.Unit;

/**
 * The Property class.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Building.class)
@JsonInclude(Include.NON_NULL)
public class Building {
  private int id;
  // @JsonManagedReference(@JsonManagedReference(value = "building-unit"))
  @JsonInclude(Include.NON_EMPTY)
  private Set<Unit> units = new LinkedHashSet<>();
  private String name;
  private String address;
  private String city;
  private String state;
  private String zipCode;
  private String description;

  public Building() {}

  public Building(
    int id,
    String name,
    String address,
    String city,
    String state,
    String zipCode,
    String description) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
    this.description = description;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Set<Unit> getUnits() {
    return units;
  }

  public void setUnits(Set<Unit> units) {
    if (units == null) {
      return;
    }

    this.units.clear();
    this.units.addAll(units);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Unit getUnitById(int id) {
    if (units == null) {
      return null;
    }

    for (Unit unit : units) {
      if (unit.getId() == id) {
        return unit;
      }
    }
    return null;
  }

  public void addUnit(Unit unit) {
    if (unit == null) {
      return;
    }

    units.add(unit);
  }

  public void removeUnit(Unit unit) {
    if (unit == null) {
      return;
    }

    units.remove(unit);
  }

  public void removeUnitById(int id) {
    var unit = getUnitById(id);
    if (unit == null) {
      return;
    }
    removeUnit(unit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, address, city, state, zipCode, description);
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

    Building other = (Building) obj;
    return id == other.id
      && name.equals(other.name)
      && address.equals(other.address)
      && city.equals(other.city)
      && state.equals(other.state)
      && zipCode.equals(other.zipCode)
      && description.equals(other.description);
  }
  // @formatter:on


  // @formatter:off
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\"building\":{");
    sb.append("\"id\":" + id);
    sb.append(",\"units\": [");
    var iterator = units.iterator();
    while (iterator.hasNext()) {
      Unit unit = iterator.next();
      sb.append(unit.toReferredString());
      if (iterator.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append("]");
    sb.append(",\"name\":\"" + name + "\"");
    sb.append(",\"address\":\"" + address + "\"");
    sb.append(",\"city\":\"" + city + "\"");
    sb.append(",\"state\":\"" + state + "\"");
    sb.append(",\"zipCode\":\"" + zipCode + "\"");
    sb.append(",\"description\":\"" + description + "\"");
    sb.append("}");
    return sb.toString();
  }
  // @formatter:on

  public String toReferredString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("\"id\":" + id);
    sb.append(",\"name\":\"" + name + "\"");
    sb.append(",\"address\":\"" + address + "\"");
    sb.append(",\"city\":\"" + city + "\"");
    sb.append(",\"state\":\"" + state + "\"");
    sb.append(",\"zipCode\":\"" + zipCode + "\"");
    sb.append(",\"description\":\"" + description + "\"");
    sb.append("}");
    return sb.toString();
  }
}
