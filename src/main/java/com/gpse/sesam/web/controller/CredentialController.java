package com.gpse.sesam.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.domain.credential.credentials.CredentialService;
import com.gpse.sesam.web.cmd.IssueCredentialAttributeCmd;
import com.gpse.sesam.web.exception.CredentialNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CredentialController {
	private final CredentialService service;

	@Autowired
	public CredentialController(final CredentialService service) {
		this.service = service;
	}

	@GetMapping("/credentials")
	public List<Credential> getCredential() {
		return service.getCredentials();
	}

	@GetMapping(value = "/credentials/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Credential credential(@PathVariable final Long id) {
		final Optional<Credential> credential = service.getCredential(id);

		if (credential.isPresent()) {
			return credential.get();
		}

		throw new CredentialNotFoundException();
	}

	@GetMapping(value = "/credentials/getByIssuer/{id}")
	@ResponseStatus(HttpStatus.OK)
	public List<Credential> getByIssuerId(@PathVariable final Long id) {
		return service.getCredentialsByIssuerId(id);
	}

	@Secured("ISSUER")
	@PostMapping("/credentials/{id}/issue")
	@ResponseStatus(HttpStatus.OK)
	public String issueCredential(@PathVariable final Long id,
								  @Valid @RequestBody final List<IssueCredentialAttributeCmd> attributeCmds)
			throws JsonProcessingException {
		return service.issueCredential(id, attributeCmds);
	}
}
