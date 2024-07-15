package hy.oltp.core.estate.lease.service;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import hy.api.core.estate.lease.Lease;
import hy.api.core.estate.lease.LeaseDetail;
import hy.api.core.estate.tenant.Tenant;
import hy.api.core.estate.unit.Unit;
import hy.oltp.core.estate.lease.persistence.LeaseDetailEntity;
import hy.oltp.core.estate.lease.persistence.LeaseEntity;
import hy.oltp.core.estate.tenant.persistence.TenantEntity;
import hy.oltp.core.estate.unit.persistence.UnitEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LeaseMapper {
  LeaseMapper INSTANCE = Mappers.getMapper(LeaseMapper.class);

  Lease entityToApi(LeaseEntity entity);

  @Mapping(target = "version", ignore = true)
  LeaseEntity apiToEntity(Lease lease);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "unit", nullValuePropertyMappingStrategy = IGNORE)
  @Mapping(target = "tenant", nullValuePropertyMappingStrategy = IGNORE)
  void updateEntityFromApi(Lease lease, @MappingTarget LeaseEntity entity);

  LeaseDetail entityToApi(LeaseDetailEntity entity);

  LeaseDetailEntity apiToEntity(LeaseDetail api);

  @Mapping(target = "building", ignore = true)
  @Mapping(target = "leases", ignore = true)
  Unit unitEntityToUnit(UnitEntity unitEntity);

  @Mapping(target = "leases", ignore = true)
  Tenant tenantEntityToTenant(TenantEntity tenantEntity);
}
