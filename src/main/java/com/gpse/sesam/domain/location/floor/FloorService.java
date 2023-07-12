package com.gpse.sesam.domain.location.floor;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FloorService {


	void deleteById(Long id);

	Floor save(Floor floor, MultipartFile file);

	Floor save(Floor floor);

	List<Floor> getAll();
}
