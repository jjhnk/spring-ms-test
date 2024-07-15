package hy.oltp.core.estate.unit.persistence;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class RoomRepositoryImpl implements RoomRepositoryCustom {
  @PersistenceContext
  private final EntityManager em;
  private final JPAQueryFactory query;

  public RoomRepositoryImpl(EntityManager em) {
    this.em = em;
    this.query = new JPAQueryFactory(em);
  }

  @Override
  @Transactional
  public RoomEntity saveByUnitId(RoomEntity room, int unitId) {
    QUnitEntity unitEntity = QUnitEntity.unitEntity;

    UnitEntity unit = query.selectFrom(unitEntity)
      .where(unitEntity.id.eq(unitId))
      .fetchOne();

    if (unit != null) {
      room.setUnit(unit);
      em.persist(room);
      return room;
    }

    throw new IllegalArgumentException("Unit with id " + unitId + " not found");
  }

  @Override
  @Transactional
  public List<RoomEntity> saveAllByUnitId(List<RoomEntity> rooms, int unitId) {
    QUnitEntity unitEntity = QUnitEntity.unitEntity;

    UnitEntity unit = query.selectFrom(unitEntity)
      .where(unitEntity.id.eq(unitId))
      .fetchOne();

    if (unit != null) {
      for (RoomEntity room : rooms) {
        room.setUnit(unit);
        em.detach(room);
        room.setId(0);
        em.persist(room);
      }
      return rooms;
    }

    throw new IllegalArgumentException("Unit with id " + unitId + " not found");
  }

  @Override
  public RoomEntity findByIdAndUnitId(int id, int unitId) {
    QRoomEntity roomEntity = QRoomEntity.roomEntity;

    return query.selectFrom(roomEntity)
      .where(roomEntity.id.eq(id)
        .and(roomEntity.unit.id.eq(unitId)))
      .fetchOne();
  }

  @Override
  public List<RoomEntity> findAllByUnitId(int unitId) {
    QRoomEntity roomEntity = QRoomEntity.roomEntity;

    return query.selectFrom(roomEntity)
      .where(roomEntity.unit.id.eq(unitId))
      .fetch();
  }

  @Override
  public void deleteByIdAndUnitId(int id, int unitId) {
    QRoomEntity roomEntity = QRoomEntity.roomEntity;

    RoomEntity room = query.selectFrom(roomEntity)
      .where(roomEntity.id.eq(id)
        .and(roomEntity.unit.id.eq(unitId)))
      .fetchOne();

    if (room != null) {
      em.remove(room);
    }
  }
}
