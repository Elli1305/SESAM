package com.gpse.sesam.domain.user;

import org.springframework.data.repository.CrudRepository;

/**
 * A repository for managing {@link SesamUser} objects in H2.
 */
public interface IssuerRepository extends CrudRepository<Issuer, Long> {
}
