package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.credential.Credential;
import com.gpse.sesam.domain.credential.CredentialService;
import com.gpse.sesam.web.exception.CredentialNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.web.cmd.IssueCredentialAttributeCmd;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/credentials")
public class CredentialController {
    private final CredentialService service;

    @Autowired
    public CredentialController(CredentialService service) {
        this.service = service;
    }

    /*
    @GetMapping("/")
    public List<Credential> getCredential() {
        return credentialService.getCredentials();
    }
    */

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Credential credential(@PathVariable Long id) {
        final Optional<Credential> credential = service.getCredential(id);

        if (credential.isPresent()) {
            return credential.get();
        }

        throw new CredentialNotFoundException();
    }

    @Secured("ISSUER")
    @PostMapping("/{id}/issue")
    @ResponseStatus(HttpStatus.OK)
    public String issueCredential(@PathVariable final Long id,
                                  @Valid @RequestBody final List<IssueCredentialAttributeCmd> attributeCmds) throws JsonProcessingException {
        return service.issueCredential(id, attributeCmds);
    }
}
