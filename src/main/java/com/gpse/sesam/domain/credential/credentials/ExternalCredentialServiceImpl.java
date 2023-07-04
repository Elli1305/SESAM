package com.gpse.sesam.domain.credential.credentials;

import com.gpse.sesam.domain.credential.category.Category;
import com.gpse.sesam.domain.credential.category.CategoryService;
import com.gpse.sesam.web.cmd.ExternalCredentialCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExternalCredentialServiceImpl implements ExternalCredentialService {
    private final ExternalCredentialRepository externalCredentialRepository;

    private final CategoryService categoryService;

    @Autowired
    public ExternalCredentialServiceImpl(ExternalCredentialRepository externalCredentialRepository, CategoryService categoryService) {
        this.externalCredentialRepository = externalCredentialRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<ExternalCredential> getExternalCredentials() {
        List<ExternalCredential> externalCredentials = new ArrayList<>();
        externalCredentialRepository.findAll().forEach(externalCredentials::add);
        return externalCredentials;
    }

    @Override
    public Optional<ExternalCredential> getExternalCredential(Long id) {
        return externalCredentialRepository.findById(id);
    }

    @Override
    public List<ExternalCredential> getExternalCredentialByCredentialDefinitionId(String id) {
        return externalCredentialRepository.findAllByCredentialDefinitionId(id);
    }

    @Override
    public List<ExternalCredentialCmd> getAllExternal() {
        List<ExternalCredential> externalCredentials = getExternalCredentials();
        List<ExternalCredentialCmd> cmds = new ArrayList<>();
        List<Category> categories = categoryService.getCategory();
        for (ExternalCredential extern : externalCredentials) {
            List<String> intern = new ArrayList<>();
            String categoryName = "-";
            String name = extern.getName();
            for (Category category : categories) {
                if (category.getExternalCredentials().contains(extern)) {
                    categoryName = category.getName();
                    for (InternalCredential internal : category.getCredentials()) {
                        intern.add(internal.getName());
                    }
                }

            }
            cmds.add(new ExternalCredentialCmd(categoryName, name, intern));

        }
        return cmds;
    }

    @Override
    public void deleteAll() {
        externalCredentialRepository.deleteAll();
    }

    @Override
    public void saveAll(Iterable<ExternalCredential> externalCredentials) {
        externalCredentialRepository.saveAll(externalCredentials);
    }
}
