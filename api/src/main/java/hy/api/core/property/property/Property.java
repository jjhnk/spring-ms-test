package hy.api.core.property.property;

/**
 * The Property class.
 */
public class Property {
  private int propertyId;
  private String name;
  private String address;
  private String city;
  private String state;
  private String zipCode;
  private String description;

  public Property() {
    this(0, null, null, null, null, null, null);
  }

  public Property(
    int propertyId,
    String name,
    String address,
    String city,
    String state,
    String zipCode,
    String description) {
    this.propertyId = propertyId;
    this.name = name;
    this.address = address;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
    this.description = description;
  }

  public int getPropertyId() {
    return propertyId;
  }

  public void setPropertyId(int propertyId) {
    this.propertyId = propertyId;
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
}
