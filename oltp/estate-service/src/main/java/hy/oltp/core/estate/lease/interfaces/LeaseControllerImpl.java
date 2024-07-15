package hy.oltp.core.estate.lease.interfaces;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hy.api.core.estate.lease.Lease;
import hy.api.core.estate.lease.LeaseController;
import hy.oltp.core.estate.lease.service.LeaseService;

@RestController
public class LeaseControllerImpl implements LeaseController {
  private final LeaseService leaseService;

  public LeaseControllerImpl(LeaseService leaseService) {
    this.leaseService = leaseService;
  }

  @Override
  public Lease createLease(@RequestBody Lease lease) {
    return leaseService.createLease(lease);
  }

  @Override
  public Lease getLease(@RequestHeader HttpHeaders headers, @PathVariable int id) {
    return leaseService.getLease(headers, id);
  }

  @Override
  public List<Lease> getLeases(
    @RequestHeader HttpHeaders headers,
    @RequestParam(value = "tenantId", required = false, defaultValue = "0") int tenantId,
    @RequestParam(value = "unitId", required = false, defaultValue = "0") int unitId) {
    return leaseService.getLeases(headers, tenantId, unitId);
  }

  @Override
  public Lease updateLease(@RequestHeader HttpHeaders headers, @PathVariable int id, @RequestBody Lease lease) {
    return leaseService.updateLease(headers, id, lease);
  }

  @Override
  public void deleteLease(@RequestHeader HttpHeaders headers, @PathVariable int id) {
    leaseService.deleteLease(headers, id);
  }

}
