package hy.oltp.core.estate.unit.persistence;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepositoryCustom {

  RoomEntity saveByUnitId(RoomEntity room, int unitId);

  List<RoomEntity> saveAllByUnitId(List<RoomEntity> rooms, int unitId);

  RoomEntity findByIdAndUnitId(int id, int unitId);

  List<RoomEntity> findAllByUnitId(int unitId);

  void deleteByIdAndUnitId(int id, int unitId);

}
