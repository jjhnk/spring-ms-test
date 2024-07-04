package hy.api.core.property.unit;

import java.util.Collections;
import java.util.List;

/**
 * Details of a unit
 *
 * @see Unit
 * @see RentDetails
 * @see Room
 */
public class UnitDetails {
  private int squareFeet;
  private RentDetails rentDetails;
  private List<Room> rooms;

  public UnitDetails() {
    this(0, null, Collections.emptyList());
  }

  public UnitDetails(int squareFeet, RentDetails rentDetails, List<Room> rooms) {
    this.squareFeet = squareFeet;
    this.rentDetails = rentDetails;
    this.rooms = rooms;
  }

  public int getSquareFeet() {
    return squareFeet;
  }

  public void setSquareFeet(int squareFeet) {
    this.squareFeet = squareFeet;
  }

  public RentDetails getRentDetails() {
    return rentDetails;
  }

  public void setRentDetails(RentDetails rentDetails) {
    this.rentDetails = rentDetails;
  }

  public List<Room> getRooms() {
    return rooms;
  }

  public void setRooms(List<Room> rooms) {
    this.rooms = rooms;
  }
}
