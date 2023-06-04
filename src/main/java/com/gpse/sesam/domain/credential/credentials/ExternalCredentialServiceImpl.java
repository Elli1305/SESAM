package com.gpse.sesam.domain.credential.credentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExternalCredentialServiceImpl implements ExternalCredentialService {
    private final ExternalCredentialRepository externalCredentialRepository;

    @Autowired
    public ExternalCredentialServiceImpl(ExternalCredentialRepository externalCredentialRepository) {
        this.externalCredentialRepository = externalCredentialRepository;
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
    public void deleteAll() {
        externalCredentialRepository.deleteAll();
    }

    @Override
    public void saveAll(Iterable<ExternalCredential> externalCredentials) {
        externalCredentialRepository.saveAll(externalCredentials);
    }
}
