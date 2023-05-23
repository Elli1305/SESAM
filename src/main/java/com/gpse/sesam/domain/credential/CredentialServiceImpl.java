package com.gpse.sesam.domain.credential;

import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.user.Issuer;
import com.gpse.sesam.web.cmd.CredentialCmd;
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
    public List<CredentialCmd> credentialFindByLocation(Long id) {
        return credentialRepository.findByLocation(id);
    }

    public static CredentialCmd createCredentialCmd(Category category, Credential credential) {
        List<String> externalCred = new ArrayList<>();
        List<String> issuerName = new ArrayList<>();
        List<String> issuerRoom = new ArrayList<>();

        for (int i = 0; i< category.getExternalCredentials().size(); i++) {
            externalCred.add( category.getExternalCredentials().get(i).getName());
        }

        for (int i = 0; i<credential.getIssuer().size(); i++) {
            issuerRoom.add(credential.getIssuer().get(i).getRoom().getName());
            issuerName.add(credential.getIssuer().get(i).getFirstName() + " " +
                    credential.getIssuer().get(i).getLastName());
        }

        CredentialCmd cmd = new CredentialCmd(category.getName(), credential.getName(), externalCred, issuerName, issuerRoom);
        return cmd;
    }
}
