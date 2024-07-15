package hy.oltp.core.estate.tenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import hy.api.core.estate.tenant.Tenant;
import hy.api.exceptions.NotFoundException;
import hy.oltp.core.estate.common.service.CacheUtility;
import hy.oltp.core.estate.tenant.persistence.TenantEntity;
import hy.oltp.core.estate.tenant.persistence.TenantRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TenantService {
  private static final String MSG_NOT_FOUND_WITH_ID = "Tenant not found with id ";
  private static final String CACHE_KEY = "tenants:";
  private final TenantRepository repository;
  private final CacheUtility<TenantEntity> cacheUtility;
  private final TenantMapper mapper;

  public TenantService(TenantRepository repository, CacheUtility<TenantEntity> cacheUtility, TenantMapper mapper) {
    this.repository = repository;
    this.cacheUtility = cacheUtility;
    this.mapper = mapper == null ? TenantMapper.INSTANCE : mapper;
  }

  public Tenant createTenant(Tenant tenant) {
    log.info("Creating tenant {}", tenant);
    var saved = repository.save(mapper.apiToEntity(tenant));
    return mapper.entityToApi(saved);
  }

  public Tenant getTenant(HttpHeaders headers, int id) {
    var entity = getEntityFromCacheOrRepository(headers, id);
    cacheUtility.safeAddToCache(headers, CACHE_KEY, entity);
    return mapper.entityToApi(entity);
  }

  public List<Tenant> getTenants(HttpHeaders headers) {
    return getEntitiesFromCacheOrRepository(headers);
  }

  public Tenant updateTenant(HttpHeaders headers, int id, Tenant tenant) {
    var found = getEntityFromCacheOrRepository(headers, id);
    mapper.updateEntityFromApi(tenant, found);
    var saved = repository.save(found);
    cacheUtility.safeAddToCache(headers, CACHE_KEY, saved);
    return mapper.entityToApi(saved);
  }

  public void deleteTenant(HttpHeaders headers, int id) {
    var found = getEntityFromCacheOrRepository(headers, id);
    repository.delete(found);
    cacheUtility.safeRemoveFromCache(headers, CACHE_KEY + id);
  }

  private TenantEntity getEntityFromCacheOrRepository(HttpHeaders headers, int id) {
    var cached = cacheUtility.safeGetFromCache(headers, CACHE_KEY, id);
    if (cached != null) {
      return cached;
    }

    return repository.findById(id)
      .orElseThrow(() -> new NotFoundException(MSG_NOT_FOUND_WITH_ID + id));
  }

  private List<Tenant> getEntitiesFromCacheOrRepository(HttpHeaders headers) {
    var cached = cacheUtility.safeGetListFromCache(headers, CACHE_KEY);
    if (cached != null && !cached.isEmpty()) {
      return cached.stream()
        .map(mapper::entityToApi)
        .collect(Collectors.toList());
    }

    return repository.findAll()
      .stream()
      .map(mapper::entityToApi)
      .collect(Collectors.toList());
  }
}
