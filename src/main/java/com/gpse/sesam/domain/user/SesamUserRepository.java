package com.gpse.sesam.domain.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * A repository for managing {@link SesamUser} objects in H2.
 */
public interface SesamUserRepository extends CrudRepository<SesamUser, String> {
	Optional<SesamUser> findByEmail(String username);
}
