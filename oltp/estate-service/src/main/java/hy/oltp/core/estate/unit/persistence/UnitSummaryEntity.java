package hy.oltp.core.estate.unit.persistence;

import java.util.Objects;

import hy.api.core.estate.unit.UnitStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnitSummaryEntity {
  @NotBlank
  private String unitNumber;

  @Min(0)
  private int floorNumber = 0;

  @NotNull
  @Enumerated(EnumType.STRING)
  private UnitStatus status;

  @Override
  public int hashCode() {
    return Objects.hash(unitNumber, floorNumber, status);
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

    UnitSummaryEntity other = (UnitSummaryEntity) obj;
    return unitNumber.equals(other.unitNumber)
      && floorNumber == other.floorNumber
      && status.equals(other.status);
  }
  // @formatter:on
}
