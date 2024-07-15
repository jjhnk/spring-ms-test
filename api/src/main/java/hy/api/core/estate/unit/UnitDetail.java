package hy.api.core.estate.unit;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Details of a unit
 *
 * @see Unit
 * @see RentDetails
 * @see Room
 */
@JsonInclude(Include.NON_NULL)
public class UnitDetail {
  @JsonInclude(Include.NON_EMPTY)
  private Set<Room> rooms = new LinkedHashSet<>();
  private int totalSquareFeet;
  private BigDecimal rent;

  public UnitDetail() {}

  @JsonCreator
  public UnitDetail(@JsonProperty("totalSquareFeet") int totalSquareFeet, @JsonProperty("rent") double rent) {
    this.totalSquareFeet = totalSquareFeet;
    this.rent = BigDecimal.valueOf(rent);
  }

  public UnitDetail(int totalSquareFeet, BigDecimal rent) {
    this.totalSquareFeet = totalSquareFeet;
    this.rent = rent;
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

  public Set<Room> getRooms() {
    return rooms;
  }

  public void setRooms(Set<Room> rooms) {
    if (rooms == null) {
      return;
    }

    this.rooms.clear();
    this.rooms.addAll(rooms);
  }

  public Room getRoomById(int id) {
    if (rooms == null) {
      return null;
    }

    for (Room room : rooms) {
      if (room.getId() == id) {
        return room;
      }
    }

    return null;
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalSquareFeet, rent.floatValue());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    UnitDetail other = (UnitDetail) obj;
    return totalSquareFeet == other.totalSquareFeet && rent.floatValue() == other.rent.floatValue();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\"unitDetail\":{");
    sb.append("\"rooms\": [");
    var iterator = rooms.iterator();
    while (iterator.hasNext()) {
      Room room = iterator.next();
      sb.append(room.toReferredString());
      if (iterator.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append("]");
    sb.append(",\"totalSquareFeet\":" + totalSquareFeet);
    sb.append(",\"rent\":" + rent);
    sb.append('}');
    return sb.toString();
  }
}
