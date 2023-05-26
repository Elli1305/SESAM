package com.gpse.sesam.domain.credential;

import com.gpse.sesam.domain.location.Location;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
	List<Category> getCategory();

	Optional<Category> getCategory(Long id);

	Optional<Category> getCategory(List<Location> locations);


	void saveAll(Iterable<Category> category);
}
