package hy.oltp.core.estate.lease.persistence;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class LeaseRepositoryImpl implements LeaseRepositoryCustom {
  @PersistenceContext
  private final EntityManager em;
  private final JPAQueryFactory query;

  public LeaseRepositoryImpl(EntityManager em) {
    this.em = em;
    this.query = new JPAQueryFactory(em);
  }

  @Override
  public List<LeaseEntity> findByTenantId(int tenantId) {
    QLeaseEntity entity = QLeaseEntity.leaseEntity;
    BooleanExpression predicate = entity.tenant.id.eq(tenantId);
    return query.selectFrom(entity).where(predicate).fetch();
  }

  @Override
  public List<LeaseEntity> findByUnitId(int unitId) {
    QLeaseEntity entity = QLeaseEntity.leaseEntity;
    BooleanExpression predicate = entity.unit.id.eq(unitId);
    return query.selectFrom(entity).where(predicate).fetch();
  }

  @Override
  public List<LeaseEntity> findByTenantIdOrUnitId(int tenantId, int unitId) {
    QLeaseEntity entity = QLeaseEntity.leaseEntity;
    BooleanExpression predicate = entity.tenant.id.eq(tenantId).or(entity.unit.id.eq(unitId));
    return query.selectFrom(entity).where(predicate).fetch();
  }

}
