package com.gpse.sesam.domain.location.floor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FloorServiceImpl implements FloorService {

	private final FloorRepository floorRepository;

	@Autowired
	public FloorServiceImpl(final FloorRepository floorRepository) {
		this.floorRepository = floorRepository;
	}

	@Override
	public void deleteById(Long id) {
		floorRepository.deleteById(id);
	}

	@Override
	public Floor save(Floor floor) {
		return floorRepository.save(floor);
	}
}
