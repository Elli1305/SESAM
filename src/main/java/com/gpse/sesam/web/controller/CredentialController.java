package com.gpse.sesam.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.domain.credential.credentials.*;
import com.gpse.sesam.web.cmd.*;
import com.gpse.sesam.web.exception.CredentialNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	private final ExternalCredentialService externalCredentialService;

	@Autowired
	public CredentialController(final CredentialService service, ExternalCredentialService externalCredentialService) {
		this.service = service;
		this.externalCredentialService = externalCredentialService;
	}

	@GetMapping("/credentials")
	public List<InternalCredential> getCredential() {
		return service.getCredentials();
	}

	@GetMapping("/external_credentials")
	public List<ExternalCredential> getExternalCredential() {
		return service.getExternalCredentials();
	}

	@GetMapping("/credentials/getAll")
	public List<Credential> getAllCredentials() {
		return service.getAllCredentials();
	}

	@GetMapping(value = "/credentials/{id}")
	@ResponseStatus(HttpStatus.OK)
	public InternalCredential credential(@PathVariable final Long id) {
		final Optional<InternalCredential> credential = service.getCredential(id);

		if (credential.isPresent()) {
			return credential.get();
		}

		throw new CredentialNotFoundException();
	}

	@GetMapping(value = "/credentials/getByIssuer/{id}")
	@ResponseStatus(HttpStatus.OK)
	public List<InternalCredential> getByIssuerId(@PathVariable final Long id) {
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

	@PostMapping(value = "/credentials")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ADMINISTRATOR")
	public void create(@Valid @RequestBody final CreateCredentialCmd credential) {
		service.create(credential);
	}

	@PutMapping(value = "/credentials/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Secured("ADMINISTRATOR")
	public void update(@PathVariable final Long id, @Valid @RequestBody final UpdateCredentialCmd credential) {
		service.update(id, credential);
	}

	@DeleteMapping(value = "/credentials/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Secured("ADMINISTRATOR")
	public void delete(@PathVariable final Long id) {
		service.delete(id);
	}

	@GetMapping(value = "/allcredentials")
	public List<CredentialCmd> credentials () { return service.getAllCredentialsForView();}

	@GetMapping(value = "/externalcredentialview")
	public List<ExternalCredentialCmd> externalCredentials() {return externalCredentialService.getAllExternal();}
}
