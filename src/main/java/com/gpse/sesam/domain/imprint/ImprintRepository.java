package com.gpse.sesam.domain.imprint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImprintRepository extends CrudRepository<Imprint, Long> {
}
