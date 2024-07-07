package hy.api.core.estate.buildings;

import java.util.Set;

import hy.api.core.estate.unit.Unit;

/**
 * The Property class.
 */
public class Building {
  private int id;
  private String name;
  private Set<Unit> units;
  private String address;
  private String city;
  private String state;
  private String zipCode;
  private String description;

  public Building() {
    this(0, null, null, null, null, null, null, null);
  }

  public Building(int id) {
    this(id, null, null, null, null, null, null, null);
  }

  public Building(
    int id,
    Set<Unit> units,
    String name,
    String address,
    String city,
    String state,
    String zipCode,
    String description) {
    this.id = id;
    this.name = name;
    this.units = units;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Unit> getUnits() {
    return units;
  }

  public void setUnits(Set<Unit> units) {
    this.units = units;
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

}
