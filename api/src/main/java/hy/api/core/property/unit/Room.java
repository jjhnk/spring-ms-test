package hy.api.core.property.unit;

/**
 * Represents a room in a unit
 *
 * @see Unit
 * @see UnitDetails
 * @see RoomTypes
 */
public class Room {
  private int propertyId;
  private int unitId;
  private int roomId;
  private String name;
  private String type;
  private int squareFeet;
  private int windowsCount;

  public Room() {
    this(0, 0, 0, null, null, 0, 0);
  }

  public Room(int propertyId, int unitId, int roomId, String name, String type, int squareFeet, int windowsCount) {
    this.propertyId = propertyId;
    this.unitId = unitId;
    this.roomId = roomId;
    this.name = name;
    this.type = type;
    this.squareFeet = squareFeet;
    this.windowsCount = windowsCount;
  }

  public int getPropertyId() {
    return propertyId;
  }

  public void setPropertyId(int propertyId) {
    this.propertyId = propertyId;
  }

  public int getUnitId() {
    return unitId;
  }

  public void setUnitId(int unitId) {
    this.unitId = unitId;
  }

  public int getRoomId() {
    return roomId;
  }

  public void setRoomId(int roomId) {
    this.roomId = roomId;
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
