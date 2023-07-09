package com.gpse.sesam.domain.credential.category;

import com.gpse.sesam.web.cmd.CategoryResponseCmd;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getCategory();

    Optional<Category> getCategory(Long id);

    void deleteById(Long id);

    void deleteAll();

    void saveAll(Iterable<Category> category);

    void updateCategory(Long id, CategoryResponseCmd cmd);

    void deleteCategory(Category category);

    void createCategory(CategoryResponseCmd categoryCmd);
}
