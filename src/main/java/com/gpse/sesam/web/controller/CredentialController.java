package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.credential.Credential;
import com.gpse.sesam.domain.credential.CredentialService;
import com.gpse.sesam.web.exception.CredentialNotFoundException;
import com.gpse.sesam.web.exception.LocationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CredentialController {
    private final CredentialService credentialService;

    @Autowired
    public CredentialController(CredentialService credentialService){
        this.credentialService = credentialService;
    }

    /*
    @GetMapping("/credentialview")
    public List<Credential> getCredential() {
        return credentialService.getCredentials();
    }

    @GetMapping("/credentialview/{id:\\d+}")
    public Credential getLocationInfo(@PathVariable("id") final Long id) {
        if (CredentialService.getCredential(id).isPresent()) {
            return CredentialService.getCredential(id).get();
        } else {
            throw new CredentialNotFoundException("Credential Not Found with ID: " + id);
        }
    }
    */
}
