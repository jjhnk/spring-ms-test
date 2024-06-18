package hy.microservices.core.product.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import hy.api.core.product.Product;
import hy.microservices.core.product.persistence.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @Mapping(target = "serviceAddress", ignore = true)
  Product entityToApi(ProductEntity entity);

  @Mapping(target = "version", ignore = true)
  @Mapping(target = "id", ignore = true)
  ProductEntity apiToEntity(Product body);
}
