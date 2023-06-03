package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.credential.category.Category;
import com.gpse.sesam.domain.credential.category.CategoryService;
import com.gpse.sesam.domain.credential.credentials.CredentialService;
import com.gpse.sesam.domain.credential.credentials.ExternalCredential;
import com.gpse.sesam.domain.credential.credentials.ExternalCredentialService;
import com.gpse.sesam.web.cmd.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CategoryController {
	private final CategoryService categoryService;
	private final CredentialService credentialService;
    private final ExternalCredentialService externalCredentialService;

	@Autowired
	public CategoryController(final CategoryService categoryService, final CredentialService credentialService,
                              ExternalCredentialService externalCredentialService) {
        this.categoryService = categoryService;
        this.credentialService = credentialService;
        this.externalCredentialService = externalCredentialService;
    }

    @GetMapping("/credentialmapping")
    public List<Category> getCategoriesInfo() {
        return categoryService.getCategory();
    }

    @Secured("ADMINISTRATOR")
    @GetMapping("/externalcredentials")
    public List<ExternalCredential> getExternalInfos() {
        return externalCredentialService.getExternalCredentials();
    }


    @Secured("ADMINISTRATOR")
    @DeleteMapping("/credentialmapping/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable("id") final Long id) {
        categoryService.deleteById(id);
    }

    @Secured("ADMINISTRATOR")
    @PutMapping("/credentialmapping/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCategory(@PathVariable("id") final Long id, @RequestBody CategoryResponseCmd cmd) {
        categoryService.updateCategory(id, cmd);
    }


    @Secured("ADMINISTRATOR")
    @PostMapping("/category")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@RequestBody final CategoryResponseCmd category) {
        categoryService.createCategory(category);
    }

    @GetMapping("/category/{id}")
    public void getCategoryById(@PathVariable("id") Long id) {
        categoryService.getCategory(id);
    }

}
