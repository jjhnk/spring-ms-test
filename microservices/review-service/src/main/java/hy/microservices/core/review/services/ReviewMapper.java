package hy.microservices.core.review.services;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import hy.api.core.review.Review;
import hy.microservices.core.review.persistence.ReviewEntity;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

  @Mapping(target = "serviceAddress", ignore = true)
  Review entityToApi(ReviewEntity entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  ReviewEntity apiToEntity(Review api);

  List<Review> entityListToApiList(List<ReviewEntity> entityList);

  List<ReviewEntity> apiListToEntityList(List<Review> apiList);
}
