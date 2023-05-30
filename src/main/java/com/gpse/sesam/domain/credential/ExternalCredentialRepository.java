package com.gpse.sesam.domain.credential;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ExternalCredentialRepository extends CrudRepository<ExternalCredential, Long> {
    @Query(value = "SELECT  ex from ExternalCredential ex WHERE ex.id =?1 ")
    ExternalCredential externalFindById(Long id);
}
