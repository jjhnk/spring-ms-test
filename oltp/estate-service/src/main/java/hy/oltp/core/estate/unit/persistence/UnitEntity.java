package hy.oltp.core.estate.unit.persistence;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import hy.oltp.core.estate.building.persistence.BuildingEntity;
import hy.oltp.core.estate.lease.persistence.LeaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "units", indexes = @Index(name = "units_unique_idx", columnList = "id"))
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(UnitEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UnitEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Version
  private int version;

  @ManyToOne
  @JoinColumn(name = "building_id", nullable = false)
  // @JsonBackReference
  private BuildingEntity building;

  @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
  // @JsonManagedReference
  @Setter(AccessLevel.NONE)
  private Set<LeaseEntity> leases = new LinkedHashSet<>();

  @Embedded
  private UnitSummaryEntity summary;

  @Embedded
  private UnitDetailEntity detail;

  public UnitEntity(UnitSummaryEntity summary, UnitDetailEntity detail) {
    this.summary = summary;
    this.detail = detail;
  }

  public void init() {
    if (summary == null) {
      summary = new UnitSummaryEntity();
    }
    if (detail == null) {
      detail = new UnitDetailEntity();
    }
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

  public LeaseEntity getLeaseById(int leaseId) {
    return leases.stream().filter(lease -> lease.getId() == leaseId).findFirst().orElse(null);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, summary, detail);
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

    UnitEntity other = (UnitEntity) obj;
    return id == other.id
      && summary.equals(other.summary)
      && detail.equals(other.detail);
  }
  // @formatter:on
}
