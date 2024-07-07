package hy.api.core.estate.lease;

import java.util.Date;

public class LeaseDetail {
  private Date startDate;
  private Date endDate;
  private double rentAmount;
  private double securityDeposit;
  private LeaseStatus status;

  public LeaseDetail(Date startDate, Date endDate, double rentAmount, double securityDeposit, LeaseStatus status) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.rentAmount = rentAmount;
    this.securityDeposit = securityDeposit;
    this.status = status;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public double getRentAmount() {
    return rentAmount;
  }

  public void setRentAmount(double rentAmount) {
    this.rentAmount = rentAmount;
  }

  public double getSecurityDeposit() {
    return securityDeposit;
  }

  public void setSecurityDeposit(double securityDeposit) {
    this.securityDeposit = securityDeposit;
  }

  public LeaseStatus getStatus() {
    return status;
  }

  public void setStatus(LeaseStatus status) {
    this.status = status;
  }

}
