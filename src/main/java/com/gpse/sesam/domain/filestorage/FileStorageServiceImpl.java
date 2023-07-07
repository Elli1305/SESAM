package com.gpse.sesam.domain.filestorage;

import com.gpse.sesam.configuration.FileStorageConfiguration;
import com.gpse.sesam.domain.colors.ColorTheme;
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

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private final Path fileStorageLocation;

	public FileStorageServiceImpl(final FileStorageConfiguration fileStorageConfiguration) {
		fileStorageLocation = Paths.get(fileStorageConfiguration.getBaseDir()).normalize();
		try {
			Files.createDirectories(fileStorageLocation);
		} catch (final IOException e) {
			throw new BeanCreationException("Could not initialize file storage", e);
		}
	}

	@Override
	public String storeFile(final MultipartFile file) {
		final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		try {

			final String randomFileName = String.format("%s.%s",
					UUID.randomUUID().toString().replace("-", ""), fileName.split("\\.")[1]);
			final Path targetLocation = fileStorageLocation.resolve(randomFileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return randomFileName;
		} catch (final IOException e) {
			throw new FileStorageException("Could not store file " + fileName, e);
		}
	}

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

	@Override
	public void reset() {
		try (final InputStream logoFile = Files.newInputStream(fileStorageLocation.resolve("T_logo_white.svg"));
			 final InputStream faviconFile = Files.newInputStream(fileStorageLocation.resolve("T_favicon.ico"))) {
			final Path logoLocation = fileStorageLocation.resolve("Logo.svg");
			final Path faviconLocation = fileStorageLocation.resolve("Favicon.ico");
			Files.copy(logoFile, logoLocation, StandardCopyOption.REPLACE_EXISTING);
			Files.copy(faviconFile, faviconLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (final IOException e) {
			throw new FileStorageException("Could not reset resources", e);
		}
	}

}