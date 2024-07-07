package hy.oltp.core.estate.tenant.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<TenantEntity, Integer> {
  List<TenantEntity> findByFirstName(String firstName);
  List<TenantEntity> findByLastName(String lastName);
  List<TenantEntity> findByEmail(String email);
}
