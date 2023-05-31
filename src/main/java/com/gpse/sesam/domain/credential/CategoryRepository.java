package com.gpse.sesam.domain.credential;

import org.hibernate.sql.Delete;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
   @Query("delete from Category cat where cat.id=?1")
    void deleteCategoryById(Long id);
}
