package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.imprint.ImprintServiceImpl;
import com.gpse.sesam.web.cmd.UpdateImprintCmd;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * The ImprintController class provides endpoints for managing Imprint entries.
 */
@RestController
@CrossOrigin
public class ImprintController {

    /**
     * The service for managing Imprint entries.
     */
    private final ImprintServiceImpl imprintService;

    /**
     * Constructs a new ImprintController with the provided Imprint service.
     *
     * @param imprintService the service for managing Imprint entries
     */
    public ImprintController(final ImprintServiceImpl imprintService) {
        this.imprintService = imprintService;
    }

    /**
     * Gets the content of the latest Imprint entry.
     *
     * @return the content of the latest Imprint entry
     */
    @GetMapping("/api/imprint")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getImprintContent() {
        String imprintContent = imprintService.getLatestImprintEntry();

        if (imprintContent == null) {
            imprintContent = "";
        }

        return new ResponseEntity<>(imprintContent, HttpStatus.OK);
    }

    /**
     * Gets the content of the latest Imprint entry.
     *
     * @return the content of the latest Imprint entry
     */
    @GetMapping("/api/imprint/latest")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getLatestImprint() {
        String latestImprintContent = imprintService.getLatestImprintEntry();

        if (latestImprintContent == null) {
            latestImprintContent = "";
        }

        return new ResponseEntity<>(latestImprintContent, HttpStatus.OK);
    }

    /**
     * Creates a new Imprint entry with the content provided in the request body.
     * This method requires ADMINISTRATOR authority.
     *
     * @param imprintContent the content of the new Imprint entry
     */
    @Secured("ADMINISTRATOR")
    @PostMapping("/api/imprint")
    @ResponseStatus(HttpStatus.OK)
    public void saveImprintContent(@RequestBody final UpdateImprintCmd imprintContent) {
        if (imprintContent != null) {
            imprintService.createImprintEntry(imprintContent.getContent());
        }
    }

    /**
     * Deletes all Imprint entries.
     * This method requires ADMINISTRATOR authority.
     */
    @Secured("ADMINISTRATOR")
    @DeleteMapping("/api/imprint")
    @ResponseStatus(HttpStatus.OK)
    public void deleteImprintContent() {
        imprintService.deleteImprintEntry();
    }
}
