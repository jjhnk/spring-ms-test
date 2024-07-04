package hy.api.core.property.tenant;

/**
 * Represents a tenant
 */
public class Tenant {
  private int tenantId;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;

  public Tenant(int tenantId, String firstName, String lastName, String email, String phoneNumber) {
    this.tenantId = tenantId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

  public int getTenantId() {
    return tenantId;
  }

  public void setTenantId(int tenantId) {
    this.tenantId = tenantId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }



}
