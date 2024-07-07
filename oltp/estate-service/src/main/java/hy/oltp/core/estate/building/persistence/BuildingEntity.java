package hy.oltp.core.estate.building.persistence;

import java.util.Set;

import hy.oltp.core.estate.unit.persistence.UnitEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "buildings",
    indexes = {@Index(name = "buildings_unique_idx", unique = true, columnList = "id")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingEntity {
  @Id
  @GeneratedValue
  private int id;

  @Version
  private int version;

  @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UnitEntity> units;

  @NotBlank
  @Size(max = 50)
  private String name;

  @NotBlank
  @Size(max = 255)
  private String address;

  @NotBlank
  @Size(max = 50)
  private String city;

  @NotBlank
  @Size(max = 50)
  private String state;

  @NotBlank
  @Size(max = 10)
  private String zipCode;

  private String description;

  public BuildingEntity(String name, String address, String city, String state, String zipCode) {
    this.name = name;
    this.address = address;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
  }
}
