package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.issuer.IssuerService;
import com.gpse.sesam.domain.user.Issuer;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/issuers")
    @ResponseStatus(HttpStatus.OK)
    public List<Issuer> getIssuers() {
        return service.getIssuers();
    }
}
