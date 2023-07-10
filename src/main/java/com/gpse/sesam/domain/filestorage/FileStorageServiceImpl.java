package com.gpse.sesam.domain.filestorage;

import com.gpse.sesam.configuration.FileStorageConfiguration;
import com.gpse.sesam.domain.colors.ColorTheme;
import com.gpse.sesam.domain.colors.ColorsService;
import com.gpse.sesam.web.exception.FileStorageException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Der Service zum Speichern von Dateien in einem Dateispeicher.
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

	private final Path fileStorageLocation;
	private final ColorsService colorsService;

	/**
	 * Konstruktor für den FileStorageServiceImpl.
	 *
	 * @param fileStorageConfiguration die Konfiguration für den Dateispeicher
	 */

	public FileStorageServiceImpl(final FileStorageConfiguration fileStorageConfiguration,
								  final ColorsService colorsService) {
		fileStorageLocation = Paths.get(fileStorageConfiguration.getBaseDir()).normalize();
		this.colorsService = colorsService;
		try {
			Files.createDirectories(fileStorageLocation);
		} catch (final IOException e) {
			throw new BeanCreationException("Could not initialize file storage", e);
		}
	}


	/**
	 * Speichert die angegebene Datei im Dateispeicher.
	 *
	 * @param file die hochgeladene Datei
	 * @return der generierte Dateiname
	 * @throws FileStorageException wenn ein Fehler beim Speichern der Datei auftritt
	 */

	@Override
	public String storeFile(final MultipartFile file) {
		final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			String[] fileNameArray = fileName.split("\\.");
			final String randomFileName = String.format("%s.%s",
					UUID.randomUUID().toString().replace("-", ""),
					fileNameArray[fileNameArray.length - 1]);
			final Path targetLocation = fileStorageLocation.resolve(randomFileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return randomFileName;
		} catch (final IOException e) {
			throw new FileStorageException("Could not store file " + fileName, e);
		}
	}

	/**
	 * Speichert das angegebene Logo im Dateispeicher, abhängig vom Farbthema.
	 *
	 * @param file        das hochgeladene Logo
	 * @param colorTheme  das Farbthema
	 * @return der Dateiname des Logos
	 * @throws FileStorageException wenn ein Fehler beim Speichern des Logos auftritt
	 */
	@Override
	public String storeLogo(final MultipartFile file, String colorTheme) {
		final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			final Path targetLocation;
			if (colorTheme.equals(ColorTheme.LIGHT.toString())) {
				targetLocation = fileStorageLocation.resolve("Logo.svg");
			} else {
				targetLocation = fileStorageLocation.resolve("Logo-Dark.svg");
			}
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return fileName;
		} catch (final IOException e) {
			throw new FileStorageException("Could not store file " + fileName, e);
		}
	}

	/**
	 * Speichert die angegebene Favicon-Datei im Dateispeicher.
	 *
	 * @param file die hochgeladene Favicon-Datei
	 * @return der Dateiname des Favicon
	 * @throws FileStorageException wenn ein Fehler beim Speichern der Favicon-Datei auftritt
	 */
	@Override
	public String storeFavicon(final MultipartFile file) {
		final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			final Path targetLocation = fileStorageLocation.resolve("Favicon.ico");
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return fileName;
		} catch (final IOException e) {
			throw new FileStorageException("Could not store file " + fileName, e);
		}
	}

	/**
	 * Setzt die Ressourcen des Dateispeichers auf die Standardwerte zurück.
	 *
	 * @throws FileStorageException wenn ein Fehler beim Zurücksetzen der Ressourcen auftritt
	 */
	@Override
	public void reset(ColorTheme colorTheme) {
		try (final InputStream logoFile = Files.newInputStream(fileStorageLocation
				.resolve(colorsService.getDefaultColors(colorTheme).getLogoPath()));
			 final InputStream faviconFile = Files.newInputStream(fileStorageLocation.resolve("T_favicon.ico"))) {
			final Path logoLocation;
			if (colorTheme.equals(ColorTheme.LIGHT)) {
				logoLocation = fileStorageLocation.resolve("Logo.svg");
			} else {
				logoLocation = fileStorageLocation.resolve("Logo-Dark.svg");
			}
			final Path faviconLocation = fileStorageLocation.resolve("Favicon.ico");
			Files.copy(logoFile, logoLocation, StandardCopyOption.REPLACE_EXISTING);
			Files.copy(faviconFile, faviconLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (final IOException e) {
			throw new FileStorageException("Could not reset resources", e);
		}
	}

}