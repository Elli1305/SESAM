package com.gpse.sesam.web;

import com.gpse.sesam.domain.SesamUser;
import com.gpse.sesam.domain.SesamUserService;
import com.gpse.sesam.web.cmd.SesamUserCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class SesamUserController {
    private final SesamUserService service;

    @Autowired
    public SesamUserController(final SesamUserService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SesamUser createUser(@RequestBody SesamUserCmd sesamUserCmd) {
        return service.createUser(sesamUserCmd);
    }
}
