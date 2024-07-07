package hy.oltp.core.estate.unit.persistence;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class UnitRepositoryImpl implements UnitRepositoryCustom {
  @PersistenceContext
  private final EntityManager em;
  private final JPAQueryFactory query;

  public UnitRepositoryImpl(EntityManager em) {
    this.em = em;
    this.query = new JPAQueryFactory(em);
  }

  @Override
  public List<UnitEntity> findByBuildingId(int buildingId) {
    QUnitEntity entity = QUnitEntity.unitEntity;
    BooleanExpression predicate = entity.building.id.eq(buildingId);
    return query.selectFrom(entity).where(predicate).fetch();

  }

  @Override
  public List<UnitEntity> findByTenantId(int tenantId) {
    QUnitEntity entity = QUnitEntity.unitEntity;
    BooleanExpression predicate = entity.leases.any().id.eq(tenantId);
    return query.selectFrom(entity).where(predicate).fetch();
  }
}
