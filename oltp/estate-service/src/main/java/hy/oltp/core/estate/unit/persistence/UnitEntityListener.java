package hy.oltp.core.estate.unit.persistence;

import jakarta.persistence.PreUpdate;

public class UnitEntityListener {
  @PreUpdate
  public void onPreUpdate(UnitEntity unitEntity) {
    unitEntity.setVersion(unitEntity.getVersion() + 1);
  }
}
