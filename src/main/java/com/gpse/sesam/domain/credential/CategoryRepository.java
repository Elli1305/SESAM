package com.gpse.sesam.domain.credential;

import org.hibernate.sql.Delete;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
   @Modifying
   @Query("delete from Category cat where cat.id=?1")
   void deleteCategoryById(Long id);

   @Modifying
   @Query("delete from Category cat where cat.name=?1")
   void deleteCategoryByName(String name);

   @Query(value="SELECT cat from Category cat where cat.id=?1")
   Category categoryGetById(Long id);

   @Query(value= "SELECT cat from Category cat WHERE cat.name =?1")
   Category getCategoriesByName(String name);
}
