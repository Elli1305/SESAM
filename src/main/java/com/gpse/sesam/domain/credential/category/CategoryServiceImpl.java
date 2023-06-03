package com.gpse.sesam.domain.credential.category;

import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.domain.credential.credentials.CredentialRepository;
import com.gpse.sesam.domain.credential.credentials.ExternalCredential;
import com.gpse.sesam.domain.credential.credentials.ExternalCredentialRepository;
import com.gpse.sesam.domain.location.Location;
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
        Optional<Category> category = getCategory(id);
        if (category.isPresent()) {
            for (Credential credential: category.get().getCredentials()) {
                credential.setCategory(null);
            }
            category.get().setCredentials(null);
            categoryRepository.deleteById(id);
        }
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
            for (Credential credential : category.get().getCredentials()) {
                credential.setCategory(null);
            }
            category.get().setCredentials(null);
            if (!cmd.getCredentials().isEmpty()) {
                List<Credential> internal = new ArrayList<>();
                for (Long cred : cmd.getCredentials()) {
                    Optional<Credential> credential = credentialRepository.findById(cred);
                    if (credential.isPresent()) {
                        credential.get().setCategory(category.get());
                        internal.add(credential.get());
                    }
                }
                category.get().setCredentials(internal);
            }
            List<ExternalCredential> external = new ArrayList<>();
            for (Long cred: cmd.getExternalCredentials() ) {
                Optional<ExternalCredential> credential = externalCredentialRepository.findById(cred);
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
        List<Credential> internal = new ArrayList<>();
        for (Long cred: categoryCmd.getCredentials() ) {
        Optional<Credential> credential = credentialRepository.findById(cred);
            if (credential.isPresent()) {
                internal.add(credential.get());
                credential.get().setCategory(category);
            }
        }
        List<ExternalCredential> external = new ArrayList<>();
        category.setCredentials(internal);
        for (Long cred: categoryCmd.getExternalCredentials() ) {
            Optional<ExternalCredential> credential = externalCredentialRepository.findById(cred);
            if (credential.isPresent()) {
                external.add(credential.get());
            }
        }
        category.setExternalCredentialList(external);
        categoryRepository.save(category);
    }
}
