package hy.oltp.core.estate.unit.persistence;

import java.util.Set;

import hy.oltp.core.estate.building.persistence.BuildingEntity;
import hy.oltp.core.estate.lease.persistence.LeaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "units", indexes = @Index(name = "units_unique_idx", columnList = "id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnitEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Version
  private int version;

  @ManyToOne
  @JoinColumn(name = "building_id", nullable = false)
  private BuildingEntity building;

  @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<LeaseEntity> leases;

  @Embedded
  private UnitSummaryEntity summary;

  @Embedded
  private UnitDetailEntity detail;

  public UnitEntity(BuildingEntity buildingEntity, UnitSummaryEntity summary, UnitDetailEntity detail) {
    this.building = buildingEntity;
    this.summary = summary;
    this.detail = detail;
  }

}
