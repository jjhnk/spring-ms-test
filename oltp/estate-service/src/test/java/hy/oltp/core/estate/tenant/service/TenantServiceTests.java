package hy.oltp.core.estate.tenant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import hy.api.core.estate.tenant.Tenant;
import hy.api.exceptions.NotFoundException;
import hy.oltp.core.estate.common.service.CacheUtility;
import hy.oltp.core.estate.tenant.persistence.TenantEntity;
import hy.oltp.core.estate.tenant.persistence.TenantRepository;

class TenantServiceTests {
  @Mock
  private TenantRepository repository;

  @Mock
  private CacheUtility<TenantEntity> cacheUtility;

  @Mock
  private TenantMapper mapper;

  @InjectMocks
  private TenantService tenantService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateTenant() {
    Tenant tenant = new Tenant(1, "firstName", "lastName", "email@email.com", "01012345678");
    TenantEntity tenantEntity = new TenantEntity("firstName", "lastName", "email@email.com", "01012345678");
    when(mapper.apiToEntity(tenant)).thenReturn(tenantEntity);
    when(repository.save(tenantEntity)).thenReturn(tenantEntity);
    when(mapper.entityToApi(tenantEntity)).thenReturn(tenant);

    Tenant result = tenantService.createTenant(tenant);

    verify(repository, times(1)).save(tenantEntity);
    assertThat(tenant).isEqualTo(result);
  }

  @Test
  void testGetTenantWithCache() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    TenantEntity tenantEntity = new TenantEntity("firstName", "lastName", "email@email.com", "01012345678");
    Tenant tenant = new Tenant(1, "firstName", "lastName", "email@email.com", "01012345678");

    when(cacheUtility.safeGetFromCache(headers, "tenants:", id)).thenReturn(tenantEntity);
    when(mapper.entityToApi(tenantEntity)).thenReturn(tenant);

    Tenant result = tenantService.getTenant(headers, id);

    verify(cacheUtility, times(1)).safeGetFromCache(headers, "tenants:", id);
    verify(mapper, times(1)).entityToApi(tenantEntity);
    assertThat(tenant).isEqualTo(result);
  }

  @Test
  void testGetTenantWithRepository() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    TenantEntity tenantEntity = new TenantEntity("firstName", "lastName", "email@email.com", "01012345678");
    Tenant tenant = new Tenant(1, "firstName", "lastName", "email@email.com", "01012345678");

    when(cacheUtility.safeGetFromCache(headers, "tenants:", id)).thenReturn(null);
    when(repository.findById(id)).thenReturn(Optional.of(tenantEntity));
    when(mapper.entityToApi(tenantEntity)).thenReturn(tenant);

    Tenant result = tenantService.getTenant(headers, id);

    verify(repository, times(1)).findById(id);
    verify(mapper, times(1)).entityToApi(tenantEntity);
    assertThat(tenant).isEqualTo(result);
  }

  @Test
  void testGetTenantNotFound() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;

    when(cacheUtility.safeGetFromCache(headers, "tenants:", id)).thenReturn(null);
    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> tenantService.getTenant(headers, id));

    verify(repository, times(1)).findById(id);
  }

  @Test
  void testGetTenantsWithCache() {
    HttpHeaders headers = new HttpHeaders();
    List<TenantEntity> tenantEntities =
      Arrays.asList(new TenantEntity("firstName", "lastName", "email@email.com", "01012345678"),
        new TenantEntity("firstName2", "lastName2", "email2@email.com", "22212345678"));
    List<Tenant> tenants = Arrays.asList(new Tenant(1, "firstName", "lastName", "email@email.com", "01012345678"),
      new Tenant(1, "firstName2", "lastName2", "email2@email.com", "22212345678"));

    when(repository.findAll()).thenReturn(tenantEntities);
    when(mapper.entityToApi(any(TenantEntity.class))).thenReturn(tenants.get(0), tenants.get(1));

    List<Tenant> result = tenantService.getTenants(headers);

    assertThat(tenants).hasSameElementsAs(result);
  }

  @Test
  void testGetTenantsWithRepository() {
    HttpHeaders headers = new HttpHeaders();
    List<TenantEntity> tenantEntities =
      Arrays.asList(new TenantEntity("firstName", "lastName", "email@email.com", "01012345678"),
        new TenantEntity("firstName2", "lastName2", "email2@email.com", "22212345678"));
    List<Tenant> tenants = Arrays.asList(new Tenant(1, "firstName", "lastName", "email@email.com", "01012345678"),
      new Tenant(1, "firstName2", "lastName2", "email2@email.com", "22212345678"));

    when(cacheUtility.safeGetListFromCache(headers, "tenants:")).thenReturn(null);
    when(repository.findAll()).thenReturn(tenantEntities);
    when(mapper.entityToApi(any(TenantEntity.class))).thenReturn(tenants.get(0), tenants.get(1));

    List<Tenant> result = tenantService.getTenants(headers);

    verify(mapper, times(tenantEntities.size())).entityToApi(any(TenantEntity.class));
    verify(repository, times(1)).findAll();
    assertThat(tenants).hasSameElementsAs(result);
  }

  @Test
  void testGetTenantsNotFound() {
    HttpHeaders headers = new HttpHeaders();

    when(cacheUtility.safeGetListFromCache(headers, "tenants:")).thenReturn(null);
    when(repository.findAll()).thenReturn(List.of());

    List<Tenant> result = tenantService.getTenants(headers);

    verify(repository, times(1)).findAll();
    assertThat(result).isEmpty();
  }

  @Test
  void testUpdateTenant() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    Tenant tenant = new Tenant(1, "firstName", "lastName", "email@email.com", "01012345678");
    TenantEntity tenantEntity = new TenantEntity("firstName", "lastName", "email@email.com", "01012345678");

    when(cacheUtility.safeGetFromCache(headers, "tenants:", id)).thenReturn(tenantEntity);
    doNothing().when(mapper).updateEntityFromApi(tenant, tenantEntity);
    when(repository.save(tenantEntity)).thenReturn(tenantEntity);
    when(mapper.entityToApi(tenantEntity)).thenReturn(tenant);

    Tenant result = tenantService.updateTenant(headers, id, tenant);

    verify(repository, times(1)).save(tenantEntity);
    verify(mapper, times(1)).entityToApi(tenantEntity);
    assertThat(tenant).isEqualTo(result);
  }

  @Test
  void testDeleteTenant() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    TenantEntity tenantEntity = new TenantEntity("firstName", "lastName", "email@email.com", "01012345678");

    when(cacheUtility.safeGetFromCache(headers, "tenants:", id)).thenReturn(tenantEntity);

    tenantService.deleteTenant(headers, id);

    verify(repository, times(1)).delete(tenantEntity);
    verify(cacheUtility, times(1)).safeRemoveFromCache(headers, "tenants:" + id);
  }
}
