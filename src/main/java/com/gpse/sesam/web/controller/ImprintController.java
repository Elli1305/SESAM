package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.imprint.ImprintServiceImpl;
import com.gpse.sesam.web.cmd.UpdateImprintCmd;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@GetMapping("/api/imprint/latest")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> getLatestImprint() {
		String latestImprintContent = imprintService.getLatestImprintEntry();

		if (latestImprintContent == null) {
			latestImprintContent = "";
		}

		return new ResponseEntity<>(latestImprintContent, HttpStatus.OK);
	}

	@PostMapping("/api/imprint")
	@ResponseStatus(HttpStatus.OK)
	public void saveImprintContent(@RequestBody final UpdateImprintCmd imprintContent) {
		if (imprintContent != null) {
			imprintService.createImprintEntry(imprintContent.getContent());
		}
	}


	@DeleteMapping("/api/imprint")
	@ResponseStatus(HttpStatus.OK)
	public void deleteImprintContent() {
		imprintService.deleteImprintEntry();
	}
}

