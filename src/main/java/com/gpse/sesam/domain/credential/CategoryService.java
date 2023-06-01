package com.gpse.sesam.domain.credential;

import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.web.cmd.CategoryResponseCmd;
import com.gpse.sesam.web.cmd.CategoryResponseCmdReplicate;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getCategory();

    Optional<Category> getCategory(Long id);

    void deleteById(Long id);


    Optional<Category> getCategory(List<Location> locations);

    void deleteAll();

    void saveAll(Iterable<Category> category);

    void updateCategory(Category category, String name, List<ExternalCredential> externalCredential, List<Credential> credential);

    void deleteCategory(final Category category);

    void deleteCategoryById(Long id);

    void createCategory(final CategoryResponseCmdReplicate categoryCmd);

    Category getCategoryById(Long id);

    void deleteCategoryByName(String name);

    Category getCategoryByName(String name);

}
