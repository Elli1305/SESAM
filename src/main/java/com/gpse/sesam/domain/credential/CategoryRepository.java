package com.gpse.sesam.domain.credential;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    @Query(value = "SELECT  cat from Category cat WHERE cat.id =?1 " )
    Category categoryFindById(Long id);
}
