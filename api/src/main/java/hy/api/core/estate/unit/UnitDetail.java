package hy.api.core.estate.unit;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Details of a unit
 *
 * @see Unit
 * @see RentDetails
 * @see Room
 */
public class UnitDetail {
  private int totalSquareFeet;
  private BigDecimal rent;
  private List<Room> rooms;

  public UnitDetail() {
    this(0, null, Collections.emptyList());
  }

  public UnitDetail(int squareFeet, BigDecimal rent, List<Room> rooms) {
    this.totalSquareFeet = squareFeet;
    this.rent = rent;
    this.rooms = rooms;
  }

  public int getTotalSquareFeet() {
    return totalSquareFeet;
  }

  public void setTotalSquareFeet(int squareFeet) {
    this.totalSquareFeet = squareFeet;
  }

  public BigDecimal getRent() {
    return rent;
  }

  public void setRent(BigDecimal rent) {
    this.rent = rent;
  }

  public List<Room> getRooms() {
    return rooms;
  }

  public void setRooms(List<Room> rooms) {
    this.rooms = rooms;
  }

}
