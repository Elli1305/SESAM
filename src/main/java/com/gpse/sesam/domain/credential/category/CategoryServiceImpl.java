package com.gpse.sesam.domain.credential.category;

import com.gpse.sesam.domain.credential.credentials.external.ExternalCredential;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredentialRepository;
import com.gpse.sesam.domain.credential.credentials.internal.CredentialRepository;
import com.gpse.sesam.domain.credential.credentials.internal.InternalCredential;
import com.gpse.sesam.web.cmd.CategoryResponseCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementierung des CategoryService, der Operationen zur Verwaltung von Kategorien durchführt.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final CredentialRepository credentialRepository;

	private final ExternalCredentialRepository externalCredentialRepository;

	/**
	 * Konstruktor für die CategoryServiceImpl-Klasse.
	 *
	 * @param categoryRepository           Das CategoryRepository.
	 * @param credentialRepository         Das CredentialRepository.
	 * @param externalCredentialRepository Das ExternalCredentialRepository.
	 */
	@Autowired
	public CategoryServiceImpl(final CategoryRepository categoryRepository,
							   final CredentialRepository credentialRepository,
							   final ExternalCredentialRepository externalCredentialRepository) {
		this.categoryRepository = categoryRepository;
		this.credentialRepository = credentialRepository;
		this.externalCredentialRepository = externalCredentialRepository;
	}

	/**
	 * Ruft alle Kategorien ab.
	 *
	 * @return Eine Liste aller vorhandenen Kategorien.
	 */
	@Override
	public List<Category> getCategory() {
		final List<Category> categories = new ArrayList<>();
		categoryRepository.findAll().forEach(categories::add);
		return categories;
	}

	/**
	 * Ruft eine Kategorie anhand der angegebenen ID ab.
	 *
	 * @param id Die ID der Kategorie.
	 * @return Die gefundene Kategorie oder Optional.empty(), wenn keine Kategorie mit der ID vorhanden ist.
	 */
	@Override
	public Optional<Category> getCategory(final Long id) {
		return categoryRepository.findById(id);
	}


	/**
	 * Löscht eine Kategorie anhand der angegebenen ID.
	 *
	 * @param id Die ID der zu löschenden Kategorie.
	 */
	@Override
	public void deleteById(final Long id) {
		Optional<Category> category = getCategory(id);
		if (category.isPresent()) {
			for (InternalCredential credential : category.get().getCredentials()) {
				credential.setCategory(null);
			}
			category.get().setCredentials(null);
			categoryRepository.deleteById(id);
		}
	}

	/**
	 * Löscht alle Kategorien.
	 */
	@Override
	public void deleteAll() {
		categoryRepository.deleteAll();
	}

	/**
	 * Speichert eine Liste von Kategorien.
	 *
	 * @param category Eine Iterable-Liste von Kategorien.
	 */
	@Override
	public void saveAll(final Iterable<Category> category) {
		categoryRepository.saveAll(category);
	}

	/**
	 * Aktualisiert eine Kategorie basierend auf der angegebenen ID und den Daten im CategoryResponseCmd-Objekt.
	 *
	 * @param id  Die ID der zu aktualisierenden Kategorie.
	 * @param cmd Das CategoryResponseCmd-Objekt mit den aktualisierten Daten.
	 */

	@Override
	public void updateCategory(final Long id, final CategoryResponseCmd cmd) {
		Optional<Category> category = getCategory(id);
		if (category.isPresent()) {
			category.get().setName(cmd.getName());
			for (InternalCredential credential : category.get().getCredentials()) {
				credential.setCategory(null);
			}

			List<InternalCredential> internal = new ArrayList<>();
			for (Long cred : cmd.getCredentials()) {
				Optional<InternalCredential> credential = credentialRepository.findById(cred);
				if (credential.isPresent()) {
					credential.get().setCategory(category.get());
					internal.add(credential.get());
				}
			}
			category.get().setCredentials(internal);

			List<ExternalCredential> external = new ArrayList<>();
			for (Long cred : cmd.getExternalCredentials()) {
				Optional<ExternalCredential> credential = externalCredentialRepository.findById(cred);
				if (credential.isPresent()) {
					credential.get().setCategory(category.get());
					external.add(credential.get());
				}
			}

			category.get().setExternalCredentials(external);
			categoryRepository.save(category.get());

		}
	}

	/**
	 * Löscht eine Kategorie.
	 *
	 * @param category Die zu löschende Kategorie.
	 */

	@Override
	public void deleteCategory(final Category category) {
		categoryRepository.delete(category);
	}


	/**
	 * Erstellt eine neue Kategorie basierend auf den Daten im CategoryResponseCmd-Objekt.
	 *
	 * @param categoryCmd Das CategoryResponseCmd-Objekt mit den Daten für die neue Kategorie.
	 */
	@Override
	public void createCategory(final CategoryResponseCmd categoryCmd) {
		final Category category = new Category(categoryCmd.getName());
		List<InternalCredential> internal = new ArrayList<>();
		for (Long cred : categoryCmd.getCredentials()) {
			Optional<InternalCredential> credential = credentialRepository.findById(cred);
			if (credential.isPresent()) {
				internal.add(credential.get());
				credential.get().setCategory(category);
			}
		}
		List<ExternalCredential> external = new ArrayList<>();
		category.setCredentials(internal);
		for (Long cred : categoryCmd.getExternalCredentials()) {
			Optional<ExternalCredential> credential = externalCredentialRepository.findById(cred);
			if (credential.isPresent()) {
				external.add(credential.get());
				credential.get().setCategory(category);
			}
		}
		category.setExternalCredentialList(external);
		categoryRepository.save(category);
	}
}
