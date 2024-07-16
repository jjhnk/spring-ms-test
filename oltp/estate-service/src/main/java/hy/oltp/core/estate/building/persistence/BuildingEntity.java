package hy.oltp.core.estate.building.persistence;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "buildings", indexes = {@Index(name = "buildings_unique_idx", unique = true, columnList = "id")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BuildingEntity {
  @Id
  @GeneratedValue
  private int id;

  @Version
  private int version;

  @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
  // @JsonManagedReference
  @JsonIgnore // TODO: resolve this to managed reference
  @Setter(AccessLevel.NONE)
  private Set<UnitEntity> units = new LinkedHashSet<>();

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

  public BuildingEntity(
    @NotBlank @Size(max = 50) String name,
    @NotBlank @Size(max = 255) String address,
    @NotBlank @Size(max = 50) String city,
    @NotBlank @Size(max = 50) String state,
    @NotBlank @Size(max = 10) String zipCode,
    String description) {
    this.name = name;
    this.address = address;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
    this.description = description;
  }

  public void setUnits(Set<UnitEntity> units) {
    if (units == null) {
      return;
    }

    this.units.clear();
    this.units.addAll(units);
  }

  public Set<UnitEntity> getUnits() {
    return units;
  }
}
