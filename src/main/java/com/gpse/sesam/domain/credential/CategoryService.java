package com.gpse.sesam.domain.credential;

import com.gpse.sesam.domain.location.Location;

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

    Category categoryGetById(Long id);

}
