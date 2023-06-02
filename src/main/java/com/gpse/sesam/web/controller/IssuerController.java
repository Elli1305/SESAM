package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.user.issuer.IssuerService;
import com.gpse.sesam.domain.user.issuer.Issuer;
import com.gpse.sesam.domain.user.issuer.IssuerServiceImpl;
import com.gpse.sesam.web.cmd.EditUserCmd;
import com.gpse.sesam.web.cmd.IssuerResponseCmd;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class IssuerController {
    private final IssuerService service;

    public IssuerController(IssuerService service) {
        this.service = service;
    }
    @Secured("ADMINISTRATOR")
    @GetMapping("/issuers")
    public List<Issuer> getIssuers() {
        return service.getIssuers();
    }

    @PutMapping("/issuer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void makeUserEdit(@PathVariable("id") final Long id, @RequestBody final IssuerResponseCmd cmd) {
       service.updateIssuer(id, cmd);
    }
}
