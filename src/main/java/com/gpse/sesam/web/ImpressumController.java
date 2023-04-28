package com.gpse.sesam.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
@SuppressWarnings("HideUtilityClassConstructor")
public class ImpressumController {
    @GetMapping("/api/impressum")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> getImpressum(HttpServletRequest request) {


        String impressumContent =
                "<p><strong>INVALID GmbH</strong></p>"
                        + "<p>Baumstr.23</p>"
                        + "<p>32124 Niemandsland</p>"
                        + "<p>Deutschland</p>"
                        + "<p>&nbsp;</p>"
                        + "<p><strong>Kontakt</strong></p>"
                        + "<p>Email: <a href=\"mailto:mail@invalid.de\">mail@invalid.de</a></p>"
                        + "<p>Telefon: 123456789</p>"
                        + "<img src=\"../../resources/Deutsche_Telekom_2022.png\" alt=\"\" "
                        + "style=\"height:73px; width:61px;\" /></p>";

        return new ResponseEntity<String>(impressumContent, HttpStatus.OK);
    }
}
