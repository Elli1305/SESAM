package com.gpse.sesam.domain.location.floor;

import com.gpse.sesam.domain.filestorage.FileStorageService;
import com.gpse.sesam.domain.location.RoomGroupServiceImpl;
import com.gpse.sesam.domain.location.building.BuildingRepository;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class FloorServiceImpl implements FloorService {

	private final FloorRepository floorRepository;

	private final FileStorageService fileStorageService;

	private final RoomRepository roomRepository;
	@Autowired
	public FloorServiceImpl(final FloorRepository floorRepository, final FileStorageService fileStorageService, RoomRepository roomRepository) {
		this.floorRepository = floorRepository;
		this.fileStorageService = fileStorageService;
		this.roomRepository = roomRepository;
	}

	@Override
	public void deleteById(final Long id) {
		Optional<Floor> floor = floorRepository.findById(id);
		if (floor.isPresent()) {
			List<Room> floorRooms = floor.get().getRooms();
			for (Room room : floorRooms) {
				roomRepository.deleteById(room.getId());
			}
		}
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
