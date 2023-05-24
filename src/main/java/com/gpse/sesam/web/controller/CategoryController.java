package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.credential.Category;
import com.gpse.sesam.domain.credential.CategoryService;
import com.gpse.sesam.domain.credential.Credential;
import com.gpse.sesam.domain.credential.CredentialService;
import com.gpse.sesam.web.cmd.CredentialCmd;
import com.gpse.sesam.web.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;
    private final CredentialService credentialService;

    @Autowired
    public CategoryController(final CategoryService categoryService, CredentialService credentialService) {
        this.categoryService = categoryService;
        this.credentialService = credentialService;
    }

    @GetMapping("/credentialview")
    public List<Category> getCategoriesInfo() {
        return categoryService.getCategory();
    }

    /*@GetMapping("/credentialview/{id:\\d+}")
    public Category getCategoryInfo(@PathVariable("id") final Long id) {
        if (categoryService.getCategory(id).isPresent()) {
            return categoryService.getCategory(id).get();
        } else {
            throw new CategoryNotFoundException("Category not found with ID:" + id);
        }
    }*/

    @GetMapping("/credentialview/{id}")
    public List<CredentialCmd> getCredentialInfos(@PathVariable("id") final Long id) {
        return credentialService.credentialFindByLocation(id);
    }
}
