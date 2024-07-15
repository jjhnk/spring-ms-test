package hy.oltp.core.estate.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository interface for all entities.
 *
 * <p>This interface extends {@code JpaRepository} and {@code QuerydslPredicateExecutor}
 * to provide common repository functionalities for any entity type.</p>
 *
 * @param <T> the type of the entity to handle, which can be any type.
 * @param <I> the type of the entity's identifier, which can be an integer, string, or any other type.
 *
 */
@NoRepositoryBean
public interface BaseRepository<T, I> extends JpaRepository<T, I>, QuerydslPredicateExecutor<T> {

}
