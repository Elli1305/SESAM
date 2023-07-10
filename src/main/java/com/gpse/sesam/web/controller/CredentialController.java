package com.gpse.sesam.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredential;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredentialService;
import com.gpse.sesam.domain.credential.credentials.internal.CredentialService;
import com.gpse.sesam.domain.credential.credentials.internal.InternalCredential;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.web.cmd.*;
import com.gpse.sesam.web.exception.CredentialNotFoundException;
import jakarta.validation.Valid;
import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.InvalidStructureException;
import org.hyperledger.indy.sdk.pool.LedgerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CredentialController {
	private final CredentialService service;
	private final ExternalCredentialService externalCredentialService;
	private final LocationService locationService;

	@Autowired
	public CredentialController(final CredentialService service, ExternalCredentialService externalCredentialService,
								LocationService locationService) {
		this.service = service;
		this.externalCredentialService = externalCredentialService;
		this.locationService = locationService;
	}

	@GetMapping("/credentials")
	public List<InternalCredential> getCredential() {
		return service.getCredentials();
	}

	@GetMapping("/external_credentials")
	public List<ExternalCredential> getExternalCredential() {
		return externalCredentialService.getExternalCredentials();
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
	public List<CredentialCmd> credentials() {
		return service.getAllCredentialsForView();
	}

	@GetMapping(value = "/externalcredentialview")
	public List<ExternalCredentialCmd> externalCredentials() {
		return externalCredentialService.getAllExternal();
	}

	@GetMapping(value = "/externalcredentialsbylocation/{id}")
	public List<ExternalCredentialCmd> externalCredentialsByLocation(@PathVariable final Long id) {
		return externalCredentialService.getAllExternalByLocation(id);
	}

	@GetMapping(value = "/allcredentialview")
	public List<AllCredentialCmd> allCredentials() {
		return service.getAllForView();
	}

	@GetMapping(value = "/allbylocation/{id}")
	public List<AllCredentialCmd> allByLocation(@PathVariable final Long id) {
		return service.getAllCredentialsByLocation(id);
	}

	@GetMapping(value = "/credential_schema/{credentialDefinitionId}")
	@ResponseStatus(HttpStatus.OK)
	public CredentialSchemaCmd getCredentialSchema(@PathVariable final String credentialDefinitionId) throws
			Exception {
		return service.getCredentialSchema(credentialDefinitionId);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IndyException.class)
	public CredentialSchemaErrorCmd indyException() {
		return new CredentialSchemaErrorCmd("ERR_LEDGER_COMMUNICATION_FAILED");
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ExecutionException.class, InterruptedException.class})
	public CredentialSchemaErrorCmd ledgerException(Exception exception) {
		final Throwable cause = exception.getCause();

		if (cause instanceof InvalidStructureException) {
			return new CredentialSchemaErrorCmd("ERR_INVALID_STRUCTURE");
		} else if (cause instanceof LedgerNotFoundException) {
			return new CredentialSchemaErrorCmd("ERR_CREDENTIAL_DEFINITION_NOT_FOUND");
		}

		return new CredentialSchemaErrorCmd("ERR_LEDGER_COMMUNICATION_FAILED");
	}
}