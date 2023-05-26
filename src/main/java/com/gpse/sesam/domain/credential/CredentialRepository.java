package com.gpse.sesam.domain.credential;

import com.gpse.sesam.web.cmd.CredentialCmd;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CredentialRepository extends CrudRepository<Credential, Long> {
    @Query(value = "SELECT cmd from CredentialCmd cmd join Credential c"
            + " join c.category cat join cat.externalCredentials ex join c.issuer i join c.doors d "
            + "join d.room r join r.floor f join f.building b join b.location l "
            + "WHERE l.id = ?1 and cmd.credentialName = c.name and cmd.categoryName = cat.name")
    List<CredentialCmd> findByLocation(Long id);
}

