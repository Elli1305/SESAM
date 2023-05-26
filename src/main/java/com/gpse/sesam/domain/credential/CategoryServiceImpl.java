package com.gpse.sesam.domain.credential;

import com.gpse.sesam.domain.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	@Autowired
	public CategoryServiceImpl(final CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<Category> getCategory() {
		final List<Category> categories = new ArrayList<>();
		categoryRepository.findAll().forEach(categories::add);
		return categories;
	}

	@Override
	public Optional<Category> getCategory(final Long id) {
		return categoryRepository.findById(id);
	}

	@Override
	public Optional<Category> getCategory(final List<Location> locations) {
		return Optional.empty();
	}

	@Override
	public void saveAll(final Iterable<Category> category) {
		categoryRepository.saveAll(category);
	}
}
