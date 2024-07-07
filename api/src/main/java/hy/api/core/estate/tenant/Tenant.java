package hy.api.core.estate.tenant;

import java.util.Set;

import hy.api.core.estate.lease.Lease;

/**
 * Represents a tenant
 */
public class Tenant {
  private int id;
  private Set<Lease> leases;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;

  public Tenant() {}

  public Tenant(int id) {
    this.id = id;
  }

  public Tenant(int id, Set<Lease> leases, String firstName, String lastName, String email, String phoneNumber) {
    this.id = id;
    this.leases = leases;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Set<Lease> getLeases() {
    return leases;
  }

  public void setLeases(Set<Lease> leases) {
    this.leases = leases;
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
