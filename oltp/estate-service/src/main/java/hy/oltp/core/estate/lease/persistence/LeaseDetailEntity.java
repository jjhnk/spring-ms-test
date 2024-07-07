package hy.oltp.core.estate.lease.persistence;

import java.util.Date;
import hy.api.core.estate.lease.LeaseStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LeaseDetailEntity {
  @NotNull
  private Date startDate;

  @NotNull
  private Date endDate;

  @Min(0)
  private double rentAmount;

  @Min(0)
  private double securityDeposit;

  @Enumerated(EnumType.STRING)
  @NotNull
  private LeaseStatus status;
}
