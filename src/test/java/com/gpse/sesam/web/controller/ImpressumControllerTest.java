package com.gpse.sesam.web.controller;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ImpressumControllerTest {
	private final String impressumText = "<p><strong>INVALID GmbH</strong></p>"
			+ "<p>Baumstr.23</p>"
			+ "<p>32124 Niemandsland</p>"
			+ "<p>Deutschland</p>"
			+ "<p>&nbsp;</p>"
			+ "<p><strong>Kontakt</strong></p>"
			+ "<p>Email: <a href=\"mailto:mail@invalid.de\">mail@invalid.de</a></p>"
			+ "<p>Telefon: 123456789</p>"
			+ "<img src=\"src/main/resources/Deutsche_Telekom_2022.png\" alt=\"\" "
			+ "style=\"height:73px; width:61px;\" /></p>";

	@Test
	void getImpressumShouldReturnHtmlString() {
		ImpressumController impressumController = new ImpressumController();

		String html = impressumController.getImpressum().getBody();

		assertThat(html, is(impressumText));
	}

}