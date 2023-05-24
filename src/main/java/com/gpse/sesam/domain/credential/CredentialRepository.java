package com.gpse.sesam.domain.credential;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CredentialRepository extends CrudRepository<Credential, Long> {
    @Query(value = "SELECT c from Credential c"
            + " join c.doors d join d.room r join r.floor f join f.building b join b.location l WHERE l.id = ?1")
    List<Credential> findByLocation(Long id);
}

