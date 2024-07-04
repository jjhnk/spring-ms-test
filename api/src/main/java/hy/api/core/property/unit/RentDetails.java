package hy.api.core.property.unit;

import java.math.BigDecimal;

/**
 * Details of rent
 *
 * @see Unit
 * @see UnitDetails
 */
public class RentDetails {
  private BigDecimal rent;
  private UnitStatus status;

  public BigDecimal getRent() {
    return rent;
  }

  public void setRent(BigDecimal rent) {
    this.rent = rent;
  }

  public UnitStatus getStatus() {
    return status;
  }

  public void setStatus(UnitStatus status) {
    this.status = status;
  }
}
