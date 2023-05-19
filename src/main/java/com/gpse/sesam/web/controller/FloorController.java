package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.file.FileStorageService;
import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.floor.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/floor")
@Secured("EDITOR")
public class FloorController {

	private final FloorService floorService;
	private final FileStorageService fileStorageService;

	@Autowired
	public FloorController(FloorService floorService, FileStorageService fileStorageService) {
		this.floorService = floorService;
		this.fileStorageService = fileStorageService;
	}

	@PostMapping("/save")
	public Floor save(Floor floor) {
		return floorService.save(floor);
	}

	@DeleteMapping("/{id:\\d+}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteById(@PathVariable("id") final Long id) {
		floorService.deleteById(id);
	}

	@PostMapping("/uploadFile")
	@ResponseStatus(HttpStatus.OK)
	public String uploadFile(@RequestParam("file") MultipartFile file) {
		String fileName = fileStorageService.storeFile(file);
		return fileName;
	}

}
