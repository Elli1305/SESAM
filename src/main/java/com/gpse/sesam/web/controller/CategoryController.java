package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.credential.Category;
import com.gpse.sesam.domain.credential.CategoryService;
import com.gpse.sesam.domain.credential.Credential;
import com.gpse.sesam.domain.credential.CredentialService;
import com.gpse.sesam.domain.credential.*;
import com.gpse.sesam.domain.user.Issuer;
import com.gpse.sesam.domain.user.SesamUser;
import com.gpse.sesam.web.cmd.CategoryCmd;
import com.gpse.sesam.web.cmd.CategoryResponseCmd;
import com.gpse.sesam.web.cmd.CredentialCmd;
import com.gpse.sesam.web.cmd.SesamUserCmd;
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
    }

    @GetMapping("/externalcredentials")
    public List<ExternalCredential> getExternalInfos() {
        return externalCredentialService.getExternalCredentials();
    }



    @DeleteMapping("/credentialmapping/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable("id") final String id) {
        categoryService.deleteCategoryById(Long.valueOf(id));
    }

     /*
    @PostMapping("/credentialmapping/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    public void updateCategory(@PathVariable("id") @RequestBody final String id, CredentialCmd cmd) {
        final Category category = categoryService.categoryGetById(Long.valueOf(id));
        categoryService.updateCategory(category, cmd.getCategoryName(), cmd.getCredentialName(), cmd.getExternalCredential());
    }*/


    /*
    @PostMapping("/externalcredential/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    public void updateExternal(@PathVariable("id") @ResponseStatus final String id, ExternalCmd dm) {
        final ExternalCredential externalCredential = externalCredentialService.findExternalById(Long.valueOf(id));
        externalCredentialService.updateExternalCredential(externalCredential, cmd.getName(), cmd.getDefintionID());
    }*/

    /*
    @DeleteMapping("/externalcredential/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteExternalCredential(@PathVariable("id") final String id) {
        final ExternalCredential externalCredential = externalCredentialService.findExternalById(Long.valueOf(id));
        externalCredentialService.deleteExternalCredential(externalCredential);
    }*/

    @PostMapping("/category")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@RequestBody final CategoryResponseCmd category) {
        categoryService.createCategory(category);
    }

}
