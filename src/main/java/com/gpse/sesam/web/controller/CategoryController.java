package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.credential.Category;
import com.gpse.sesam.domain.credential.CategoryService;
import com.gpse.sesam.domain.credential.CredentialService;
import com.gpse.sesam.web.cmd.CredentialCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CategoryController {
	private final CategoryService categoryService;
	private final CredentialService credentialService;

	@Autowired
	public CategoryController(final CategoryService categoryService, final CredentialService credentialService) {
		this.categoryService = categoryService;
		this.credentialService = credentialService;
	}

	@GetMapping("/credentialview")
	public List<Category> getCategoriesInfo() {
		return categoryService.getCategory();
	}

	@GetMapping("/credentialview/{id}")
	public List<CredentialCmd> getCredentialInfos(@PathVariable("id") final Long id) {
		return credentialService.getCredentialByLocation(id);
	}
}
