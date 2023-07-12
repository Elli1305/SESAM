package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.user.issuer.Issuer;
import com.gpse.sesam.domain.user.issuer.IssuerService;
import com.gpse.sesam.web.cmd.IssuerResponseCmd;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class IssuerController {
	private final IssuerService service;

	public IssuerController(final IssuerService service) {
		this.service = service;
	}

	@GetMapping("/issuers")
	public List<Issuer> getIssuers() {
		return service.getIssuers();
	}
	@Secured("ADMINISTRATOR")
	@PutMapping("/issuer")
	@ResponseStatus(HttpStatus.OK)
	public void updateIssuer(@RequestBody final IssuerResponseCmd cmd) {
		service.updateIssuer(cmd);
	}
}