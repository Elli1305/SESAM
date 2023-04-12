package com.gpse.sesam.domain;

import org.springframework.data.repository.CrudRepository;

/**
 * A repository for managing {@link SesamUser} objects in H2.
 */
public interface SesamUserRepository extends CrudRepository<SesamUser, String> {}
