package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.credential.*;
import com.gpse.sesam.domain.user.Issuer;
import com.gpse.sesam.web.cmd.CategoryCmd;
import com.gpse.sesam.web.cmd.CredentialCmd;
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
    public CategoryController(final CategoryService categoryService, CredentialService credentialService,
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
            cmd.add(new CategoryCmd(name, credentials, externalcredentials));
        }
        return cmd;
    }

    @GetMapping("/externalcredentials")
    public List<ExternalCredential> getExternalInfos() {
        return externalCredentialService.getExternalCredentials();
    }

    @GetMapping("/credentialview/{id}")
    public List<CredentialCmd> getCredentialInfos(@PathVariable("id") final Long id) {
        List<Credential> credentials = credentialService.credentialFindByLocation(id);
        List<CredentialCmd> credentialCmds = new ArrayList<>();
        for (Credential credential: credentials) {
            String catName = credential.getCategory().getName();
            String credentialName = credential.getName();
            List<String> external = new ArrayList<>();
            List<String> issuerName = new ArrayList<>();
            List<String> room = new ArrayList<>();
            for (ExternalCredential externalCredential: credential.getCategory().getExternalCredentials()) {
                external.add(externalCredential.getName());
            }
            for (Issuer issuer : credential.getIssuer()) {
                room.add(issuer.getRoom().getName());
                issuerName.add(issuer.getFirstName()+ " " + issuer.getLastName());
            }
            credentialCmds.add(new CredentialCmd(catName,credentialName,external,issuerName,room));
        }
        return credentialCmds;
    }

    @DeleteMapping("/credentialmapping/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable("id") final String id) {
        final Category category = categoryService.categoryGetById(Long.valueOf(id));
        categoryService.deleteCategory(category);
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

    @DeleteMapping("/externalcredential/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteExternalCredential(@PathVariable("id") final String id) {
        final ExternalCredential externalCredential = externalCredentialService.findExternalById(Long.valueOf(id));
        externalCredentialService.deleteExternalCredential(externalCredential);
    }
}
