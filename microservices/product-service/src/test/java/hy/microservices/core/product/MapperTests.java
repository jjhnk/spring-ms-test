package hy.microservices.core.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import hy.api.core.product.Product;
import hy.microservices.core.product.persistence.ProductEntity;
import hy.microservices.core.product.services.ProductMapper;

class MapperTests {

  private ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

  @Test
  void mapperTests() {
    assertNotNull(mapper);

    Product api = new Product(1, "n", 1, "a");
    ProductEntity entity = mapper.apiToEntity(api);
    assertThat(api.getProductId()).isEqualTo(entity.getProductId());
    assertThat(api.getName()).isEqualTo(entity.getName());
    assertThat(api.getWeight()).isEqualTo(entity.getWeight());

    Product api2 = mapper.entityToApi(entity);
    assertThat(api2.getProductId()).isEqualTo(entity.getProductId());
    assertThat(api2.getName()).isEqualTo(entity.getName());
    assertThat(api2.getWeight()).isEqualTo(entity.getWeight());
    assertThat(api2.getServiceAddress()).isNull();
    }

}
