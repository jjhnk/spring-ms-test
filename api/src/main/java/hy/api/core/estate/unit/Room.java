package hy.api.core.estate.unit;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Represents a room in a unit
 *
 * @see Unit
 * @see UnitDetail
 * @see RoomTypes
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Room.class)
@JsonInclude(Include.NON_NULL)
public class Room {
  private int id;
  private Unit unit;
  private String name;
  private RoomTypes type;
  private int squareFeet;
  private int windowsCount;

  public Room() {}

  public Room(int id, String name, RoomTypes type, int squareFeet, int windowsCount) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.squareFeet = squareFeet;
    this.windowsCount = windowsCount;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RoomTypes getType() {
    return type;
  }

  public void setType(RoomTypes type) {
    this.type = type;
  }

  public int getSquareFeet() {
    return squareFeet;
  }

  public void setSquareFeet(int squareFeet) {
    this.squareFeet = squareFeet;
  }

  public int getWindowsCount() {
    return windowsCount;
  }

  public void setWindowsCount(int windowsCount) {
    this.windowsCount = windowsCount;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type, squareFeet, windowsCount);
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

    Room other = (Room) obj;
    return name.equals(other.name)
      && type.equals(other.type)
      && squareFeet == other.squareFeet
      && windowsCount == other.windowsCount;
  }
  // @formatter:on


  @Override
  // @formatter:off
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\"room\":{");
    sb.append("\"id\":" + id);
    if (unit != null) {
      sb.append(",\"unit\":" + unit.toReferredString());
    }
    sb.append(",\"name\":\"" + name + "\"");
    sb.append(",\"type\":\"" + type + "\"");
    sb.append(",\"squareFeet\":" + squareFeet);
    sb.append(",\"windowsCount\":" + windowsCount);
    sb.append("}");
    return sb.toString();
  }
  // @formatter:on

  public String toReferredString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("\"id\":" + id);
    sb.append(",\"name\":\"" + name + "\"");
    sb.append(",\"type\":\"" + type + "\"");
    sb.append(",\"squareFeet\":" + squareFeet );
    sb.append(",\"windowsCount\":" + windowsCount );
    sb.append("}");
    return sb.toString();
  }
}
