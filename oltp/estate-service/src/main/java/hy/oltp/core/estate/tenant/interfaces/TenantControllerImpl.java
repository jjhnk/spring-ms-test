package hy.oltp.core.estate.tenant.interfaces;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import hy.api.core.estate.tenant.Tenant;
import hy.api.core.estate.tenant.TenantController;
import hy.oltp.core.estate.tenant.service.TenantService;

@RestController
public class TenantControllerImpl implements TenantController {
  private final TenantService service;

  public TenantControllerImpl(TenantService service) {
    this.service = service;
  }

  @Override
  public Tenant createTenant(@RequestBody Tenant tenant) {
    return service.createTenant(tenant);
  }

  @Override
  public Tenant getTenant(@RequestHeader HttpHeaders headers, @PathVariable int id) {
    return service.getTenant(headers, id);
  }

  @Override
  public List<Tenant> getTenants(@RequestHeader HttpHeaders headers) {
    return service.getTenants(headers);
  }

  @Override
  public Tenant updateTenant(@RequestHeader HttpHeaders headers, @PathVariable int id, @RequestBody Tenant tenant) {
    return service.updateTenant(headers, id, tenant);
  }

  @Override
  public void deleteTenant(@RequestHeader HttpHeaders headers, @PathVariable int id) {
    service.deleteTenant(headers, id);
  }

}
