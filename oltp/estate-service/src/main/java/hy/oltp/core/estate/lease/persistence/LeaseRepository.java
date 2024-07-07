package hy.oltp.core.estate.lease.persistence;

import hy.oltp.core.estate.common.repository.BaseRepository;

public interface LeaseRepository extends BaseRepository<LeaseEntity, Integer>, LeaseRepositoryCustom {

}
