package hy.oltp.core.estate.tenant.persistence;

import java.util.Set;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tenants",
    indexes = {@Index(name = "tenants_unique_idx", unique = true, columnList = "id")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TenantEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Version
  private int version;

  @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<LeaseEntity> leases;

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

  public TenantEntity(String firstName, String lastName, String email, String phoneNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }
}
