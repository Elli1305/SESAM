package com.gpse.sesam.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.domain.credential.Credential;
import com.gpse.sesam.domain.credential.CredentialService;
import com.gpse.sesam.web.cmd.CreateCredentialCmd;
import com.gpse.sesam.web.cmd.IssueCredentialAttributeCmd;
import com.gpse.sesam.web.exception.CredentialNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CredentialController {
    private final CredentialService service;

    @Autowired
    public CredentialController(CredentialService service) {
        this.service = service;
    }

    @GetMapping("/credentials")
    public List<Credential> getCredential() {
        return service.getCredentials();
    }

    @GetMapping(value = "/credentials/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Credential credential(@PathVariable Long id) {
        final Optional<Credential> credential = service.getCredential(id);

        if (credential.isPresent()) {
            return credential.get();
        }

        throw new CredentialNotFoundException();
    }

    @Secured("ISSUER")
    @PostMapping("/credentials/{id}/issue")
    @ResponseStatus(HttpStatus.OK)
    public String issueCredential(@PathVariable final Long id,
                                  @Valid @RequestBody final List<IssueCredentialAttributeCmd> attributeCmds)
            throws JsonProcessingException {
        return service.issueCredential(id, attributeCmds);
    }

    @PostMapping(value = "/credentials")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateCredentialCmd create(@Valid @RequestBody CreateCredentialCmd credential) {
        return credential;
    }

    @PutMapping(value = "/credentials/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreateCredentialCmd update(@PathVariable Long id, @Valid @RequestBody CreateCredentialCmd credential) {
        return credential;
    }
}
