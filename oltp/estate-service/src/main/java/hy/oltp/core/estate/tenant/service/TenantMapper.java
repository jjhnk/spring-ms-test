package hy.oltp.core.estate.tenant.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import hy.api.core.estate.lease.Lease;
import hy.api.core.estate.tenant.Tenant;
import hy.oltp.core.estate.lease.persistence.LeaseEntity;
import hy.oltp.core.estate.lease.service.LeaseMapper;
import hy.oltp.core.estate.tenant.persistence.TenantEntity;

@Mapper(componentModel = "spring", uses = {LeaseMapper.class})
public interface TenantMapper {
  TenantMapper INSTANCE = Mappers.getMapper(TenantMapper.class);

  @Named("entityToApi")
  Tenant entityToApi(TenantEntity entity);

  @Named("entityToApiWithout")
  @Mapping(target = "leases", ignore = true)
  Tenant entityToApiWithout(TenantEntity entity);

  @Named("apiToEntity")
  @Mapping(target = "version", ignore = true)
  TenantEntity apiToEntity(Tenant tenant);

  @Named("apiToEntityWithout")
  @Mapping(target = "leases", ignore = true)
  @Mapping(target = "version", ignore = true)
  TenantEntity apiToEntityWithout(Tenant tenant);

  @Mapping(target = "version", ignore = true)
  void updateEntityFromApi(Tenant api, @MappingTarget TenantEntity entity);

  default Set<Lease> leaseEntitiesToApis(Set<LeaseEntity> entities) {
    return entities.stream()
      .map(LeaseMapper.INSTANCE::entityToApiWithout)
      .collect(Collectors.toSet());
  }

  default Set<LeaseEntity> leaseApisToEntities(Set<Lease> leases) {
    return leases.stream()
      .map(LeaseMapper.INSTANCE::apiToEntityWithout)
      .collect(Collectors.toSet());
  }

  default Lease leaseEntityToApi(LeaseEntity entity) {
    return LeaseMapper.INSTANCE.entityToApiWithout(entity);
  }

  default LeaseEntity leaseApiToEntity(Lease lease) {
    return LeaseMapper.INSTANCE.apiToEntityWithout(lease);
  }
}
