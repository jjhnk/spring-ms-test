package hy.oltp.core.estate.lease.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import hy.api.core.estate.lease.Lease;
import hy.api.core.estate.lease.LeaseDetail;
import hy.api.core.estate.tenant.Tenant;
import hy.api.core.estate.unit.Unit;
import hy.oltp.core.estate.lease.persistence.LeaseEntity;
import hy.oltp.core.estate.tenant.persistence.TenantEntity;
import hy.oltp.core.estate.tenant.service.TenantMapper;
import hy.oltp.core.estate.unit.persistence.UnitEntity;
import hy.oltp.core.estate.unit.service.UnitMapper;

@Mapper(componentModel = "spring")
public interface LeaseMapper {
  LeaseMapper INSTANCE = Mappers.getMapper(LeaseMapper.class);

  @Named("entityToApi")
  Lease entityToApi(LeaseEntity entity);

  @Named("entityToApiWithout")
  @Mapping(target = "tenant", ignore = true)
  @Mapping(target = "unit", ignore = true)
  Lease entityToApiWithout(LeaseEntity entity);

  @Named("apiToEntity")
  @Mapping(target = "version", ignore = true)
  LeaseEntity apiToEntity(Lease lease);

  @Named("apiToEntityWithout")
  @Mapping(target = "tenant", ignore = true)
  @Mapping(target = "unit", ignore = true)
  @Mapping(target = "version", ignore = true)
  LeaseEntity apiToEntityWithout(Lease lease);

  Lease leaseDetailEntityToApi(LeaseDetail leaseDetail);

  LeaseDetail leaseDetailApiToEntity(LeaseDetail leaseDetail);



  default UnitEntity unitApiToEntity(Unit unit) {
    return UnitMapper.INSTANCE.apiToEntityWithout(unit);
  }

  default Unit unitEntityToApi(UnitEntity unitEntity) {
    return UnitMapper.INSTANCE.entityToApiWithout(unitEntity);
  }

  default TenantEntity tenantApiToEntity(Lease lease) {
    return TenantMapper.INSTANCE.apiToEntityWithout(lease.getTenant());
  }

  default Tenant tenantEntityToApi(TenantEntity entity) {
    return TenantMapper.INSTANCE.entityToApiWithout(entity);
  }
}
