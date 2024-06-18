package hy.microservices.core.review.persistence;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository extends CrudRepository<ReviewEntity, Integer> {
  @Transactional
  List<ReviewEntity> findByProductId(int productId);
}
