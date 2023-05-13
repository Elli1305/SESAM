package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.imprint.ImprintServiceImpl;
import com.gpse.sesam.web.cmd.UpdateImprintCmd;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
@Secured("ADMINISTRATOR")
public class ImprintController {

	@Autowired
	private ImprintServiceImpl imprintService;

	@GetMapping("/api/imprint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> getImprintContent(HttpServletRequest request) {
		String imprintContent = imprintService.getLatestImprintEntry();

		if (imprintContent == null) {
			imprintContent = "";
		}

		return new ResponseEntity<>(imprintContent, HttpStatus.OK);
	}

	@Secured("ADMINISTRATOR")
	@PostMapping(value = "/api/imprint")
	@ResponseStatus(HttpStatus.OK)
	public void saveImprintContent(@RequestBody UpdateImprintCmd imprintContent) {
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
