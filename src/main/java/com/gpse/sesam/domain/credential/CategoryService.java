package com.gpse.sesam.domain.credential;

import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.web.cmd.CategoryCmd;
import com.gpse.sesam.web.cmd.CategoryResponseCmd;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getCategory();

    Optional<Category> getCategory(Long id);

    void deleteById(Long id);


    Optional<Category> getCategory(List<Location> locations);

    void deleteAll();

    void saveAll(Iterable<Category> category);

    void updateCategory(Long id, CategoryResponseCmd cmd);

    void deleteCategory(final Category category);

    void createCategory(final CategoryResponseCmd categoryCmd);
}
