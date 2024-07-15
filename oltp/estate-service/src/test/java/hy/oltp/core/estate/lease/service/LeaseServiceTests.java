package hy.oltp.core.estate.lease.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import hy.api.core.estate.lease.Lease;
import hy.api.core.estate.lease.LeaseDetail;
import hy.api.core.estate.lease.LeaseStatus;
import hy.api.exceptions.NotFoundException;
import hy.oltp.core.estate.common.service.CacheUtility;
import hy.oltp.core.estate.lease.persistence.LeaseDetailEntity;
import hy.oltp.core.estate.lease.persistence.LeaseEntity;
import hy.oltp.core.estate.lease.persistence.LeaseRepository;

class LeaseServiceTests {

  @Mock
  private LeaseRepository repository;

  @Mock
  private CacheUtility<LeaseEntity> cacheUtility;

  @Mock
  private LeaseMapper mapper;

  @InjectMocks
  private LeaseService leaseService;

  private LeaseDetail leaseDetail = new LeaseDetail(Instant.now(), Instant.now(), 10.0, 10.0, LeaseStatus.ACTIVE);
  private LeaseDetail leaseDetail2 = new LeaseDetail(Instant.now(), Instant.now(), 11.0, 11.0, LeaseStatus.EXPIRED);
  private LeaseDetailEntity leaseDetailEntity =
    new LeaseDetailEntity(Instant.now(), Instant.now(), 10.0, 10.0, LeaseStatus.ACTIVE);
  private LeaseDetailEntity leaseDetailEntity2 =
    new LeaseDetailEntity(Instant.now(), Instant.now(), 11.0, 11.0, LeaseStatus.EXPIRED);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateLease() {
    Lease lease = new Lease(1, leaseDetail);
    LeaseEntity leaseEntity = new LeaseEntity(leaseDetailEntity);
    when(mapper.apiToEntity(lease)).thenReturn(leaseEntity);
    when(repository.save(leaseEntity)).thenReturn(leaseEntity);
    when(mapper.entityToApi(leaseEntity)).thenReturn(lease);

    Lease result = leaseService.createLease(lease);

    verify(repository, times(1)).save(leaseEntity);
    assertThat(lease).isEqualTo(result);
  }

  @Test
  void testGetLeaseWithCache() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    LeaseEntity leaseEntity = new LeaseEntity(leaseDetailEntity);
    Lease lease = new Lease(1, leaseDetail);

    when(cacheUtility.safeGetFromCache(headers, "leases:", id)).thenReturn(leaseEntity);
    when(mapper.entityToApi(leaseEntity)).thenReturn(lease);

    Lease result = leaseService.getLease(headers, id);

    verify(cacheUtility, times(1)).safeGetFromCache(headers, "leases:", id);
    verify(mapper, times(1)).entityToApi(leaseEntity);
    assertThat(lease).isEqualTo(result);
  }

  @Test
  void testGetLeaseWithRepository() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    LeaseEntity leaseEntity = new LeaseEntity(leaseDetailEntity);
    Lease lease = new Lease(1, leaseDetail);

    when(cacheUtility.safeGetFromCache(headers, "leases:", id)).thenReturn(null);
    when(repository.findById(id)).thenReturn(Optional.of(leaseEntity));
    when(mapper.entityToApi(leaseEntity)).thenReturn(lease);

    Lease result = leaseService.getLease(headers, id);

    verify(repository, times(1)).findById(id);
    verify(mapper, times(1)).entityToApi(leaseEntity);
    assertThat(lease).isEqualTo(result);
  }

  @Test
  void testGetLeaseNotFound() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;

    when(cacheUtility.safeGetFromCache(headers, "leases:", id)).thenReturn(null);
    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> leaseService.getLease(headers, id));

    verify(repository, times(1)).findById(id);
  }

  @Test
  void testGetLeasesWithCache() {
    HttpHeaders headers = new HttpHeaders();
    List<LeaseEntity> leaseEntities =
      Arrays.asList(new LeaseEntity(leaseDetailEntity), new LeaseEntity(leaseDetailEntity2));
    List<Lease> leases = Arrays.asList(new Lease(1, leaseDetail), new Lease(2, leaseDetail2));

    when(cacheUtility.safeGetListFromCache(headers, "leases:")).thenReturn(leaseEntities);
    when(mapper.entityToApi(any(LeaseEntity.class))).thenReturn(leases.get(0), leases.get(1));

    List<Lease> result = leaseService.getLeases(headers, 0, 0);

    verify(mapper, times(leaseEntities.size())).entityToApi(any(LeaseEntity.class));
    assertThat(leases).hasSameElementsAs(result);
  }

  @Test
  void testGetLeasesWithRepository() {
    HttpHeaders headers = new HttpHeaders();
    List<LeaseEntity> leaseEntities =
      Arrays.asList(new LeaseEntity(leaseDetailEntity), new LeaseEntity(leaseDetailEntity2));
    List<Lease> leases = Arrays.asList(new Lease(1, leaseDetail), new Lease(2, leaseDetail2));

    when(cacheUtility.safeGetListFromCache(headers, "leases:")).thenReturn(null);
    when(repository.findAll()).thenReturn(leaseEntities);
    when(mapper.entityToApi(any(LeaseEntity.class))).thenReturn(leases.get(0), leases.get(1));

    List<Lease> result = leaseService.getLeases(headers, 0, 0);

    verify(mapper, times(leaseEntities.size())).entityToApi(any(LeaseEntity.class));
    verify(repository, times(1)).findAll();
    assertThat(leases).hasSameElementsAs(result);
  }

  @Test
  void testGetLeasesByTenantId() {
    HttpHeaders headers = new HttpHeaders();
    int tenantId = 1;
    List<LeaseEntity> leaseEntities = Arrays.asList(new LeaseEntity(leaseDetailEntity));
    List<Lease> leases = Arrays.asList(new Lease(1, leaseDetail));

    when(repository.findByTenantId(tenantId)).thenReturn(leaseEntities);
    when(mapper.entityToApi(any(LeaseEntity.class))).thenReturn(leases.get(0));

    List<Lease> result = leaseService.getLeases(headers, tenantId, 0);

    verify(repository, times(1)).findByTenantId(tenantId);
    verify(mapper, times(leaseEntities.size())).entityToApi(any(LeaseEntity.class));
    assertThat(leases).hasSameElementsAs(result);
  }

  @Test
  void testGetLeasesByUnitId() {
    HttpHeaders headers = new HttpHeaders();
    int unitId = 1;
    List<LeaseEntity> leaseEntities = Arrays.asList(new LeaseEntity(leaseDetailEntity));
    List<Lease> leases = Arrays.asList(new Lease(1, leaseDetail));

    when(repository.findByUnitId(unitId)).thenReturn(leaseEntities);
    when(mapper.entityToApi(any(LeaseEntity.class))).thenReturn(leases.get(0));

    List<Lease> result = leaseService.getLeases(headers, 0, unitId);

    verify(repository, times(1)).findByUnitId(unitId);
    verify(mapper, times(leaseEntities.size())).entityToApi(any(LeaseEntity.class));
    assertThat(leases).hasSameElementsAs(result);
  }

  @Test
  void testGetLeasesNotFound() {
    HttpHeaders headers = new HttpHeaders();

    when(cacheUtility.safeGetListFromCache(headers, "leases:")).thenReturn(null);
    when(repository.findAll()).thenReturn(List.of());

    List<Lease> result = leaseService.getLeases(headers, 0, 0);

    verify(repository, times(1)).findAll();
    assertThat(result).isEmpty();
  }

  @Test
  void testUpdateLease() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    Lease lease = new Lease(1, leaseDetail);
    LeaseEntity leaseEntity = new LeaseEntity(leaseDetailEntity);

    when(cacheUtility.safeGetFromCache(headers, "leases:", id)).thenReturn(leaseEntity);
    doNothing().when(mapper)
      .updateEntityFromApi(lease, leaseEntity);
    when(repository.save(leaseEntity)).thenReturn(leaseEntity);
    when(mapper.entityToApi(leaseEntity)).thenReturn(lease);

    Lease result = leaseService.updateLease(headers, id, lease);

    verify(repository, times(1)).save(leaseEntity);
    verify(mapper, times(1)).entityToApi(leaseEntity);
    assertThat(lease).isEqualTo(result);
  }

  @Test
  void testDeleteLease() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    LeaseEntity leaseEntity = new LeaseEntity(leaseDetailEntity);

    when(cacheUtility.safeGetFromCache(headers, "leases:", id)).thenReturn(leaseEntity);

    leaseService.deleteLease(headers, id);

    verify(repository, times(1)).delete(leaseEntity);
    verify(cacheUtility, times(1)).safeRemoveFromCache(headers, "leases:" + id);
  }

  @Test
  void testGetLeaseCacheMiss() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;

    when(cacheUtility.safeGetFromCache(headers, "leases:", id)).thenReturn(null);
    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> leaseService.getLease(headers, id));

    verify(cacheUtility, times(1)).safeGetFromCache(headers, "leases:", id);
    verify(repository, times(1)).findById(id);
  }

  @Test
  void testUpdateCacheAfterGetLease() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    LeaseEntity leaseEntity = new LeaseEntity(leaseDetailEntity);
    Lease lease = new Lease(1, leaseDetail);

    when(cacheUtility.safeGetFromCache(headers, "leases:", id)).thenReturn(null);
    when(repository.findById(id)).thenReturn(Optional.of(leaseEntity));
    when(mapper.entityToApi(leaseEntity)).thenReturn(lease);

    Lease result = leaseService.getLease(headers, id);

    verify(repository, times(1)).findById(id);
    verify(mapper, times(1)).entityToApi(leaseEntity);
    verify(cacheUtility, times(1)).safeAddToCache(headers, "leases:", leaseEntity);
    assertThat(lease).isEqualTo(result);
  }

  @Test
  void testCacheClearAfterDeleteLease() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    LeaseEntity leaseEntity = new LeaseEntity(leaseDetailEntity);

    when(cacheUtility.safeGetFromCache(headers, "leases:", id)).thenReturn(leaseEntity);

    leaseService.deleteLease(headers, id);

    verify(repository, times(1)).delete(leaseEntity);
    verify(cacheUtility, times(1)).safeRemoveFromCache(headers, "leases:" + id);
  }
}
