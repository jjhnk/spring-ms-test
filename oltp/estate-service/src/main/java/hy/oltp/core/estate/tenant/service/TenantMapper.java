package hy.oltp.core.estate.tenant.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import hy.api.core.estate.tenant.Tenant;
import hy.oltp.core.estate.tenant.persistence.TenantEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TenantMapper {
  TenantMapper INSTANCE = Mappers.getMapper(TenantMapper.class);

  Tenant entityToApi(TenantEntity entity);

  @Mapping(target = "version", ignore = true)
  TenantEntity apiToEntity(Tenant tenant);

  @Mapping(target = "version", ignore = true)
  @Mapping(target = "id", ignore = true)
  void updateEntityFromApi(Tenant api, @MappingTarget TenantEntity entity);
}
