package hy.api.core.estate.unit;

/**
 * Represents a room in a unit
 *
 * @see Unit
 * @see UnitDetail
 * @see RoomTypes
 */
public class Room {
  private int id;
  private Unit unit;
  private String name;
  private String type;
  private int squareFeet;
  private int windowsCount;

  public Room() {
    this(0, null, null, null, 0, 0);
  }

  public Room(int id) {
    this(id, null, null, null, 0, 0);
  }

  public Room(int id, Unit unit, String name, String type, int squareFeet, int windowsCount) {
    this.id = id;
    this.unit = unit;
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
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


}
