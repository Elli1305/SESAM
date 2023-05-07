package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.credential.Category;
import com.gpse.sesam.domain.credential.CategoryService;
import com.gpse.sesam.web.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController (final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/credentialview")
    public List<Category> getCategoriesInfo() {
        return categoryService.getCategory();
    }

    @GetMapping("/credentialview/{id:\\d+}")
    public Category getCategoryInfo(@PathVariable("id") final Long id){
        if (categoryService.getCategory(id).isPresent()) {
            return categoryService.getCategory(id).get();
        } else {
            throw new CategoryNotFoundException("Category not found with ID:" + id);
        }
    }
}
