package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.imprint.ImprintServiceImpl;
import com.gpse.sesam.web.cmd.UpdateImprintCmd;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin

public class ImprintController {

	private final ImprintServiceImpl imprintService;

	public ImprintController(final ImprintServiceImpl imprintService) {
		this.imprintService = imprintService;
	}

	@GetMapping("/api/imprint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> getImprintContent() {
		String imprintContent = imprintService.getLatestImprintEntry();

		if (imprintContent == null) {
			imprintContent = "";
		}

		return new ResponseEntity<>(imprintContent, HttpStatus.OK);
	}

	@Secured("ADMINISTRATOR")
	@PostMapping("/api/imprint")
	@ResponseStatus(HttpStatus.OK)
	public void saveImprintContent(@RequestBody final UpdateImprintCmd imprintContent) {
		if (imprintContent != null) {
			imprintService.createImprintEntry(imprintContent.getContent());
		}
	}

	@Secured("ADMINISTRATOR")
	@DeleteMapping("/api/imprint")
	@ResponseStatus(HttpStatus.OK)
	public void deleteImprintContent() {
		imprintService.deleteImprintEntry();
	}


}
