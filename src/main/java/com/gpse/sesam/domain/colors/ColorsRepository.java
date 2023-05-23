package com.gpse.sesam.domain.colors;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorsRepository extends CrudRepository<Colors, Long> {

    Colors findByDefaultColorsIsTrue();

    Colors findByDefaultColorsIsFalse();

}
