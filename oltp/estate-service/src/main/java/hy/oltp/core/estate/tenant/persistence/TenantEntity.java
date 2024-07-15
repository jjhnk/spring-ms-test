package hy.oltp.core.estate.tenant.persistence;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import hy.oltp.core.estate.lease.persistence.LeaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tenants", indexes = {@Index(name = "tenants_unique_idx", unique = true, columnList = "id")})
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TenantEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Version
  private int version;

  @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
  // @JsonManagedReference
  @Setter(AccessLevel.NONE)
  private Set<LeaseEntity> leases = new LinkedHashSet<>();

  @NotBlank
  @Size(max = 50)
  private String firstName;

  @NotBlank
  @Size(max = 50)
  private String lastName;

  @Email
  @NotBlank
  @Column(unique = true)
  @Size(max = 100)
  private String email;

  @Pattern(regexp = "\\+?\\d+")
  @Size(max = 20)
  private String phoneNumber;

  public TenantEntity(
    @NotBlank @Size(max = 50) String firstName,
    @NotBlank @Size(max = 50) String lastName,
    @Email @NotBlank @Size(max = 100) String email,
    @Pattern(regexp = "\\+?\\d+") @Size(max = 20) String phoneNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

  public Set<LeaseEntity> getLeases() {
    return leases;
  }

  public void setLeases(Set<LeaseEntity> leases) {
    if (leases == null) {
      return;
    }

    this.leases.clear();
    this.leases.addAll(leases);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, version, firstName, lastName, email, phoneNumber);
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

    TenantEntity other = (TenantEntity) obj;
    return id == other.id
      && version == other.version
      && firstName.equals(other.firstName)
      && lastName.equals(other.lastName)
      && email.equals(other.email)
      && phoneNumber.equals(other.phoneNumber);
  }
  // @formatter:on

}
