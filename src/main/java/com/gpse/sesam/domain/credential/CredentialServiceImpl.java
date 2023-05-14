package com.gpse.sesam.domain.credential;

import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.web.exception.LocationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CredentialServiceImpl implements CredentialService {

    private final CredentialRepository credentialRepository;

    @Autowired
    public CredentialServiceImpl(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Override
    public List<Credential> getCredentials() {
        final List<Credential> credentials = new ArrayList<>();
        credentialRepository.findAll().forEach(credentials::add);
        return credentials;
    }

    @Override
    public Optional<Credential> getCredential(Long id) {
        return credentialRepository.findById(id);
    }

    @Override
    public Optional<Credential> getCredential(List<Location> locations) {
        return null;
    }

    @Override
    public void deleteAll() {
        credentialRepository.deleteAll();
    }

    @Override
    public void saveAll(Iterable<Credential> credentials) {
        credentialRepository.saveAll(credentials);
    }

    @Override
    public Optional<Credential> credentialFindByLocation(Location location) {
        return Optional.empty();
    }


    @Override
    public List<Credential> credentialFindByLocation(Long id) {
        return credentialRepository.findByLocation(id);
    }
}
