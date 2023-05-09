package com.gpse.sesam.domain.credential;

import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.user.Issuer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getCategory() {
        final List<Category> categories = new ArrayList<>();
        categoryRepository.findAll().forEach(categories::add);
        return categories;
    }

    @Override
    public Optional<Category> getCategory(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> getCategory(List<Location> locations) {
        return null;
    }


    @Override
    public void deleteAll() {
        categoryRepository.deleteAll();
    }

    @Override
    public void saveAll(Iterable<Category> category) {
        categoryRepository.saveAll(category);
    }
}
