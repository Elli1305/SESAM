package com.gpse.sesam.web.controller;

import com.gpse.sesam.web.cmd.CreateCredentialCmd;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/credentials")
public class CredentialController {
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CreateCredentialCmd create(@Valid @RequestBody CreateCredentialCmd credential) {
        return credential;
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreateCredentialCmd update(@PathVariable Long id, @Valid @RequestBody CreateCredentialCmd credential) {
        return credential;
    }
}
