package hy.oltp.core.estate.unit.persistence;

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
  private int floorNumber;

  @NotNull
  @Enumerated(EnumType.STRING)
  private UnitStatus status;
}
