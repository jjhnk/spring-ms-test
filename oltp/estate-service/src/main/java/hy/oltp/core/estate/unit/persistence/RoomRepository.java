package hy.oltp.core.estate.unit.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity, Integer>, RoomRepositoryCustom {

}
