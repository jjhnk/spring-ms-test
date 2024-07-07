package hy.oltp.core.estate.building.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<BuildingEntity, Integer> {

  List<BuildingEntity> findByAddress(String address);

  List<BuildingEntity> findByCity(String city);

  List<BuildingEntity> findByState(String state);

  List<BuildingEntity> findByZipCode(String zipCode);

  List<BuildingEntity> findByName(String name);
}
