package com.gpse.sesam.domain.credential.credentials;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CredentialRepository extends CrudRepository<Credential, Long> {
	@Query(value = "SELECT cr.* FROM CREDENTIAL AS cr "
			+ "INNER JOIN DOOR_CREDENTIALS AS cd ON cd.CREDENTIALS_ID = cr.ID "
			+ "INNER JOIN DOOR AS dr ON dr.ID = cd.DOOR_ID "
			+ "INNER JOIN ROOM AS rm ON rm.ID = dr.ROOM_ID "
			+ "INNER JOIN FLOOR AS fl ON fl.ID = rm.FLOOR_ID "
			+ "INNER JOIN BUILDING AS bd ON bd.ID = fl.BUILDING_ID "
			+ "INNER JOIN LOCATION AS lc ON lc.ID = bd.LOCATION_ID "
			+ "WHERE lc.id = :id", nativeQuery = true)
	List<Credential> findByLocation(@Param("id") Long id);

	List<Credential> findAllByCredentialDefinitionId(String credentialDefinitionId);
}

