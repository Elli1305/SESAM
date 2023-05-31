package com.gpse.sesam.domain.credential;

import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.web.cmd.CategoryResponseCmd;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getCategory();

    Optional<Category> getCategory(Long id);


    Optional<Category> getCategory(List<Location> locations);

    void deleteAll();

    void saveAll(Iterable<Category> category);

    public void updateCategory(Category category, String name, List<ExternalCredential> externalCredential, List<Credential> credential);

    public void deleteCategory(final Category category);

    public void deleteCategoryById(Long id);

    public void createCategory(final CategoryResponseCmd categoryCmd);

}
