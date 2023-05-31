package com.gpse.sesam.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpse.sesam.domain.location.floor.Floor;
import com.gpse.sesam.domain.location.floor.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/floor")
@Secured("EDITOR")
public class FloorController {

	private final FloorService floorService;

	@Autowired
	public FloorController(final FloorService floorService) {
		this.floorService = floorService;
	}

	@PostMapping(path = "/uploadImage", consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public Floor save(final String floor, @RequestPart("file") final MultipartFile file)
			throws JsonProcessingException {
		return floorService.save(new ObjectMapper().readValue(floor, Floor.class), file);
	}

	@PostMapping(path = "/save")
	@ResponseStatus(HttpStatus.CREATED)
	public Floor save(@RequestBody final Floor floor) {
		return floorService.save(floor);

	}

	@DeleteMapping("/{id:\\d+}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteById(@PathVariable("id") final Long id) {
		floorService.deleteById(id);
	}

}
