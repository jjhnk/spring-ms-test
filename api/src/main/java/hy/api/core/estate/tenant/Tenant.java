package hy.api.core.estate.tenant;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import hy.api.core.estate.lease.Lease;

/**
 * Represents a tenant
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Tenant.class)
@JsonInclude(Include.NON_NULL)
public class Tenant {
  private int id;
  // @JsonManagedReference(@JsonManagedReference(value = "tenant-lease"))
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private Set<Lease> leases = new LinkedHashSet<>();
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;

  public Tenant() {}

  public Tenant(int id, String firstName, String lastName, String email, String phoneNumber) {
    this.id = id;
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
    if (leases == null) {
      leases = new LinkedHashSet<>();
    }

    this.leases.clear();
    this.leases.addAll(leases);
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

  public Lease getLeaseById(int id) {
    if (leases == null) {
      return null;
    }

    for (Lease lease : leases) {
      if (lease.getId() == id) {
        return lease;
      }
    }
    return null;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, email, phoneNumber);
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

    Tenant other = (Tenant) obj;
    return id == other.id
      && firstName.equals(other.firstName)
      && lastName.equals(other.lastName)
      && email.equals(other.email)
      && phoneNumber.equals(other.phoneNumber);
  }
  // @formatter:on

  @Override
  // @formatter:off
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\"tenant\":{");
    sb.append("\"id\":" + id);
    sb.append(",\"leases\": [");
    Iterator<Lease> iterator = leases.iterator();
    while (iterator.hasNext()) {
      Lease lease = iterator.next();
      sb.append(lease.toReferedString());
      if (iterator.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append("]");
    sb.append(",\"firstName\":\"" + firstName + "\"");
    sb.append(",\"lastName\":\"" + lastName + "\"");
    sb.append(",\"email\":\"" + email + "\"");
    sb.append(",\"phoneNumber\":\"" + phoneNumber + "\"");
    sb.append('}');
    return sb.toString();
  }
  // @formatter:on

  public String toReferredString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\"tenant\":{");
    sb.append("\"id\":" + id);
    sb.append(",\"firstName\":\"" + firstName + "\"");
    sb.append(",\"lastName\":\"" + lastName + "\"");
    sb.append(",\"email\":\"" + email + "\"");
    sb.append(",\"phoneNumber\":\"" + phoneNumber + "\"");
    sb.append('}');
    return sb.toString();
  }
}
