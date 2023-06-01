package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.credential.Category;
import com.gpse.sesam.domain.credential.CategoryService;
import com.gpse.sesam.domain.credential.Credential;
import com.gpse.sesam.domain.credential.CredentialService;
import com.gpse.sesam.domain.credential.*;
import com.gpse.sesam.domain.user.Issuer;
import com.gpse.sesam.domain.user.SesamUser;
import com.gpse.sesam.web.cmd.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    /*
    @Secured("ADMINISTRATOR")
    @GetMapping("/credentialmapping")
	public List<CategoryCmd> getCategoriesInfo() {
		List<Category> categories = categoryService.getCategory();
        List<CategoryCmd> cmd = new ArrayList<>();
        for (Category category : categories) {
            Long id = category.getId();
            String name = category.getName();
            List<String> credentials = new ArrayList<>();
            List<String> externalcredentials = new ArrayList<>();
            List<Credential> credentialList = category.getCredentials();
            for (Credential cred : credentialList) {
                credentials.add(cred.getName());
            }
            for (ExternalCredential externalCredential: category.getExternalCredentials()) {
                externalcredentials.add(externalCredential.getName());
            }
            cmd.add(new CategoryCmd(id, name, credentials, externalcredentials));
        }
        return cmd;
    }*/

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
    public void deleteCategory(@PathVariable("id") final String id) {
        categoryService.deleteById(Long.valueOf(id));
    }

    @Secured("ADMINISTRATOR")
    @PutMapping("/credentialmapping/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCategory(@PathVariable("id") final Long id, @RequestBody CategoryResponseCmd cmd) {
        categoryService.updateCategory(id, cmd);
    }

    /*
    @Secured("ADMINISTRATOR")
    @PostMapping(value = "/credentialmapping/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCategory(@PathVariable("id") @RequestBody final Long id, CategoryResponseCmdReplicate cmd) {
        final Optional<Category> category = categoryService.getCategory(id);
        if (category.isPresent()) {
            categoryService.updateCategory(category.get(), cmd.getName(), cmd.getExternalCredentials(), cmd.getCredentials());
        }
    }*/

    @Secured("ADMINISTRATOR")
    @PostMapping("/category")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@RequestBody final CategoryResponseCmd category) {
        categoryService.createCategory(category);
    }

    /*@Secured("ADMINISTRATOR")
    @DeleteMapping("/category/delete/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategoryByName(@PathVariable("name") final String name) {
        categoryService.deleteCategoryByName(name);
    }*/

    @Secured("ADMINISTRATOR")
    @DeleteMapping("/category/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategoryByName(@PathVariable("id") final Long id) {
        categoryService.deleteCategoryById(id);
    }

    @GetMapping("/category/{id}")
    public void getCategoryById(@PathVariable("id") final String id) {
        categoryService.getCategory(Long.valueOf(id));
    }

    @GetMapping("/categoryname/{name}")
    public void getCategoryByName(@PathVariable("name") final String name) {
        categoryService.getCategoryByName(name);
    }
}
