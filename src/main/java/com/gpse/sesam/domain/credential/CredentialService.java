package com.gpse.sesam.domain.credential;

import com.gpse.sesam.domain.location.Location;

import java.util.List;
import java.util.Optional;

public interface CredentialService {
    List<Credential> getCredentials();

   Optional<Credential> getCredential(Long id);

   Optional<Credential> getCredential(List<Location> locations);

    void deleteAll();

    void saveAll(Iterable<Credential> credentials);
}
