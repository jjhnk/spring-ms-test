package hy.microservices.core.product.persistence;

import static java.util.stream.IntStream.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import hy.microservices.core.product.MongoDbTestBase;

@DataMongoTest
class PersistenceTests extends MongoDbTestBase {

  @Autowired
  private ProductRepository repository;
  private ProductEntity savedEntity;

  @BeforeEach
  void setupDb() {
    repository.deleteAll();
    ProductEntity entity = new ProductEntity(1, "n", 1);
    savedEntity = repository.save(entity);
    assertEqualsProduct(entity, savedEntity);
  }

  @Test
  void create() {
    ProductEntity newEntity = new ProductEntity(2, "n", 2);
    repository.save(newEntity);

    ProductEntity foundEntity = repository.findById(newEntity.getId()).get();
    assertEqualsProduct(newEntity, foundEntity);

    assertEquals(2, repository.count());
  }

  @Test
  void update() {
    savedEntity.setName("n2");
    repository.save(savedEntity);

    ProductEntity foundEntity = repository.findById(savedEntity.getId()).get();
    assertEqualsProduct(savedEntity, foundEntity);

    assertEquals("n2", foundEntity.getName());
  }

  @Test
  void delete() {
    repository.delete(savedEntity);
    assertFalse(repository.existsById(savedEntity.getId()));
  }

  @Test
  void duplicateError() {
    ProductEntity entity = new ProductEntity(savedEntity.getProductId(), "n", 1);

    assertThrows(DuplicateKeyException.class, () -> repository.save(entity));
  }

  @Test
  void optimisticLockError() {
    ProductEntity entity1 = repository.findById(savedEntity.getId()).get();
    ProductEntity entity2 = repository.findById(savedEntity.getId()).get();

    entity1.setName("n1");
    repository.save(entity1);

    entity2.setName("n2");
    assertThrows(OptimisticLockingFailureException.class, () -> {
      repository.save(entity2);
    });

    ProductEntity updatedEntity = repository.findById(savedEntity.getId()).get();
    assertEquals(1, (int) updatedEntity.getVersion());
    assertEquals("n1", updatedEntity.getName());
  }

  @Test
  void paging() {
    repository.deleteAll();
    List<ProductEntity> newProducts =
        rangeClosed(1001, 1010).mapToObj(i -> new ProductEntity(i, "name" + i, i)).collect(Collectors.toList());
    repository.saveAll(newProducts);

    Pageable nextPage = PageRequest.of(0, 4, Direction.ASC, "productId");
    nextPage = testNextPage(nextPage, "[1001, 1002, 1003, 1004]", true);
    nextPage = testNextPage(nextPage, "[1005, 1006, 1007, 1008]", true);
    testNextPage(nextPage, "[1009, 1010]", false);
  }

  Pageable testNextPage(Pageable nextPage, String expectedProductIds, boolean expectsNextPage) {
    Page<ProductEntity> productPage = repository.findAll(nextPage);
    assertEquals(expectedProductIds,
        productPage.getContent().stream().map(p -> p.getProductId()).collect(Collectors.toList()).toString());
    assertEquals(expectsNextPage, productPage.hasNext());
    return productPage.nextPageable();
  }

  private void assertEqualsProduct(ProductEntity expectedEntity, ProductEntity actualEntity) {
    assertEquals(expectedEntity.getId(), actualEntity.getId());
    assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
    assertEquals(expectedEntity.getProductId(), actualEntity.getProductId());
    assertEquals(expectedEntity.getName(), actualEntity.getName());
    assertEquals(expectedEntity.getWeight(), actualEntity.getWeight());
  }
}
