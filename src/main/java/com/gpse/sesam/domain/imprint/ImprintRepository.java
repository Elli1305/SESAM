package com.gpse.sesam.domain.imprint;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface represents a Repository for the {@link Imprint} entity.
 * It extends Spring Data's {@link CrudRepository} interface to benefit from
 * built-in methods for common CRUD operations.
 *
 * The {@link Repository} annotation indicates that this interface is a repository,
 * which is a mechanism for encapsulating storage, retrieval, and search behavior.
 */
@Repository
public interface ImprintRepository extends CrudRepository<Imprint, Long> {
}
