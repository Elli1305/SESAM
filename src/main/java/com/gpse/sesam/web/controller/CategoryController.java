package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.credential.*;
import com.gpse.sesam.domain.user.Issuer;
import com.gpse.sesam.web.cmd.CategoryCmd;
import com.gpse.sesam.web.cmd.CredentialCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
}
