package hy.microservices.core.recommendation;

import static org.assertj.core.api.Assertions.*;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import hy.api.core.recommendation.Recommendation;
import hy.microservices.core.recommendation.persistence.RecommendationEntity;
import hy.microservices.core.recommendation.services.RecommendationMapper;

class MapperTests {
  private RecommendationMapper mapper = Mappers.getMapper(RecommendationMapper.class);

  @Test
  void mapperTests() {
    assertThat(mapper).isNotNull();

    Recommendation api = new Recommendation(1, 2, "Author 1", 3, "Content 1", "adr");
    RecommendationEntity entity = mapper.apiToEntity(api);

    assertThat(api.getProductId()).isEqualTo(entity.getProductId());
    assertThat(api.getRecommendationId()).isEqualTo(entity.getRecommendationId());
    assertThat(api.getAuthor()).isEqualTo(entity.getAuthor());
    assertThat(api.getRate()).isEqualTo(entity.getRating());
    assertThat(api.getContent()).isEqualTo(entity.getContent());

    Recommendation api2 = mapper.entityToApi(entity);

    assertThat(api.getProductId()).isEqualTo(api2.getProductId());
    assertThat(api.getRecommendationId()).isEqualTo(api2.getRecommendationId());
    assertThat(api.getAuthor()).isEqualTo(api2.getAuthor());
    assertThat(api.getRate()).isEqualTo(api2.getRate());
    assertThat(api.getContent()).isEqualTo(api2.getContent());
    assertThat(api2.getServiceAddress()).isNull();
  }

  @Test
  void mapperListTests() {
    assertThat(mapper).isNotNull();

    Recommendation api = new Recommendation(1, 2, "Author 1", 3, "Content 1", "adr");
    List<Recommendation> apiList = Collections.singletonList(api);
    List<RecommendationEntity> entityList = mapper.apiListToEntityList(apiList);
    assertThat(apiList).hasSameSizeAs(entityList);

    for (int i = 0; i < apiList.size(); i++) {
      assertThat(apiList.get(i).getProductId()).isEqualTo(entityList.get(i).getProductId());
      assertThat(apiList.get(i).getRecommendationId()).isEqualTo(entityList.get(i).getRecommendationId());
      assertThat(apiList.get(i).getAuthor()).isEqualTo(entityList.get(i).getAuthor());
      assertThat(apiList.get(i).getRate()).isEqualTo(entityList.get(i).getRating());
      assertThat(apiList.get(i).getContent()).isEqualTo(entityList.get(i).getContent());
    }

    List<Recommendation> api2List = mapper.entityListToApiList(entityList);
    assertThat(apiList).hasSameSizeAs(api2List);
    for (int i = 0; i < apiList.size(); i++) {
      assertThat(apiList.get(i).getProductId()).isEqualTo(api2List.get(i).getProductId());
      assertThat(apiList.get(i).getRecommendationId()).isEqualTo(api2List.get(i).getRecommendationId());
      assertThat(apiList.get(i).getAuthor()).isEqualTo(api2List.get(i).getAuthor());
      assertThat(apiList.get(i).getRate()).isEqualTo(api2List.get(i).getRate());
      assertThat(apiList.get(i).getContent()).isEqualTo(api2List.get(i).getContent());
    }
  }
}
