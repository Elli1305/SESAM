package com.gpse.sesam.domain.credential;

import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.web.cmd.CategoryCmd;
import com.gpse.sesam.web.cmd.CategoryResponseCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CredentialRepository credentialRepository;

    private final ExternalCredentialRepository externalCredentialRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CredentialRepository credentialRepository,
                               ExternalCredentialRepository externalCredentialRepository) {
        this.categoryRepository = categoryRepository;
        this.credentialRepository = credentialRepository;
        this.externalCredentialRepository = externalCredentialRepository;
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
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }


    @Override
    public Optional<Category> getCategory(List<Location> locations) {
        return Optional.empty();
    }


    @Override
    public void deleteAll() {
        categoryRepository.deleteAll();
    }

    @Override
    public void saveAll(Iterable<Category> category) {
        categoryRepository.saveAll(category);
    }

    @Override
    public void updateCategory(Long id, CategoryResponseCmd cmd) {
        Optional<Category> category = getCategory(id);
        if (category.isPresent()) {
            category.get().setName(cmd.getName());
            List<Credential> credentials = new ArrayList<>();
            for (Long cred: cmd.getCredentials() ) {
                Optional<Credential> credential = credentialRepository.findById((cred));
                if (credential.isPresent()) {
                    credentials.add(credential.get());
                }
            }
            List<ExternalCredential> external = new ArrayList<>();
            category.get().setCredentials(credentials);
            for (Long cred: cmd.getExternalCredentials() ) {
                Optional<ExternalCredential> credential = externalCredentialRepository.findById((cred));
                if (credential.isPresent()) {
                    external.add(credential.get());
                }
            }

            category.get().setExternalCredentials(external);
            categoryRepository.save(category.get());

        }
    }

    @Override
    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }


    @Override
    public void createCategory(CategoryResponseCmd categoryCmd) {
        final Category category = new Category(categoryCmd.getName());
        List<Credential> credentials = new ArrayList<>();
        for (Long cred: categoryCmd.getCredentials() ) {
        Optional<Credential> credential = credentialRepository.findById((cred));
            if (credential.isPresent()) {
                credentials.add(credential.get());
            }
        }
        List<ExternalCredential> external = new ArrayList<>();
        category.setCredentials(credentials);
        for (Long cred: categoryCmd.getExternalCredentials() ) {
            Optional<ExternalCredential> credential = externalCredentialRepository.findById((cred));
            if (credential.isPresent()) {
                external.add(credential.get());
            }
        }
        category.setExternalCredentialList(external);
        categoryRepository.save(category);
    }

}
