package com.gpse.sesam.domain.user.issuer;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IssuerRepository extends CrudRepository<Issuer, String> {
    Optional<Issuer> findByEmail(String username);

}
