package com.gpse.sesam.domain.location.floor;

import com.gpse.sesam.domain.filestorage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FloorServiceImpl implements FloorService {

	private final FloorRepository floorRepository;

	private final FileStorageService fileStorageService;

	@Autowired
	public FloorServiceImpl(final FloorRepository floorRepository, final FileStorageService fileStorageService) {
		this.floorRepository = floorRepository;
		this.fileStorageService = fileStorageService;
	}

	@Override
	public void deleteById(final Long id) {
		floorRepository.deleteById(id);
	}

	@Override
	public Floor save(final Floor floor, final MultipartFile multipartFile) {
		final String filePath = fileStorageService.storeFile(multipartFile);
		floor.setFloorPlanPath(filePath);
		return floorRepository.save(floor);
	}

	@Override
	public Floor save(final Floor floor) {
		return floorRepository.save(floor);
	}
}
