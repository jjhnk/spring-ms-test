package hy.oltp.core.estate.lease.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import hy.api.core.estate.lease.Lease;
import hy.api.exceptions.NotFoundException;
import hy.oltp.core.estate.common.service.CacheUtility;
import hy.oltp.core.estate.lease.persistence.LeaseEntity;
import hy.oltp.core.estate.lease.persistence.LeaseRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LeaseService {
  private static final String MSG_NOT_FOUND_WITH_ID = "Lease not found with id ";
  private static final String CACHE_KEY = "leases:";

  private final LeaseRepository repository;
  private final CacheUtility<LeaseEntity> cacheUtility;
  private final LeaseMapper mapper;

  public LeaseService(LeaseRepository repository, CacheUtility<LeaseEntity> cacheUtility, LeaseMapper mapper) {
    this.repository = repository;
    this.cacheUtility = cacheUtility;
    this.mapper = mapper == null
      ? LeaseMapper.INSTANCE
      : mapper;
  }

  public Lease createLease(Lease lease) {
    log.info("Creating lease {}", lease);
    var saved = repository.save(mapper.apiToEntity(lease));
    return mapper.entityToApi(saved);
  }

  public Lease getLease(HttpHeaders headers, int id) {
    var entity = getEntityFromCacheOrRepository(headers, id);
    cacheUtility.safeAddToCache(headers, CACHE_KEY + id, entity);
    return mapper.entityToApi(entity);
  }

  public List<Lease> getLeases(HttpHeaders headers, int tenantId, int unitId) {
    if (tenantId == 0 && unitId == 0) {
      return getEntitiesFromCacheOrRepository(headers);
    }

    List<LeaseEntity> leaseEntities;

    if (tenantId > 0 && unitId > 0) {
      leaseEntities = repository.findByTenantIdAndUnitId(tenantId, unitId);
    } else if (tenantId > 0) {
      leaseEntities = repository.findByTenantId(tenantId);
    } else if (unitId > 0) {
      leaseEntities = repository.findByUnitId(unitId);
    } else {
      leaseEntities = new ArrayList<>();
    }

    log.info("Found {} leases", leaseEntities.size());

    return leaseEntities.stream()
      .map(mapper::entityToApi)
      .collect(Collectors.toList());
  }

  public Lease updateLease(HttpHeaders headers, int id, Lease lease) {
    var found = getEntityFromCacheOrRepository(headers, id);
    mapper.updateEntityFromApi(lease, found);
    var saved = repository.save(found);
    cacheUtility.safeAddToCache(headers, CACHE_KEY + id, saved);
    return mapper.entityToApi(saved);
  }

  public void deleteLease(HttpHeaders headers, int id) {
    var found = getEntityFromCacheOrRepository(headers, id);
    repository.delete(found);
    cacheUtility.safeRemoveFromCache(headers, CACHE_KEY + id);
  }

  private LeaseEntity getEntityFromCacheOrRepository(HttpHeaders headers, int id) {
    var cached = cacheUtility.safeGetFromCache(headers, CACHE_KEY, id);
    if (cached != null) {
      return cached;
    }

    return repository.findById(id)
      .orElseThrow(() -> new NotFoundException(MSG_NOT_FOUND_WITH_ID + id));
  }

  private List<Lease> getEntitiesFromCacheOrRepository(HttpHeaders headers) {
    // TODO: consider whether caching or not entity when post
    // var cached = cacheUtility.safeGetListFromCache(headers, CACHE_KEY);
    // if (cached != null && !cached.isEmpty()) {
    //   return cached.stream()
    //     .map(mapper::entityToApi)
    //     .collect(Collectors.toList());
    // }

    return repository.findAll()
      .stream()
      .map(mapper::entityToApi)
      .collect(Collectors.toList());
  }

}