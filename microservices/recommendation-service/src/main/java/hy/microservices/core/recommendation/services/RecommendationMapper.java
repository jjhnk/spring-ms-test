package hy.microservices.core.recommendation.services;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import hy.api.core.recommendation.Recommendation;
import hy.microservices.core.recommendation.persistence.RecommendationEntity;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

  @Mapping(target = "rate", source = "entity.rating")
  @Mapping(target = "serviceAddress", ignore = true)
  Recommendation entityToApi(RecommendationEntity entity);

  @Mapping(target = "rating", source = "api.rate")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  RecommendationEntity apiToEntity(Recommendation api);

  List<Recommendation> entityListToApiList(List<RecommendationEntity> entityList);

  List<RecommendationEntity> apiListToEntityList(List<Recommendation> apiList);
}
