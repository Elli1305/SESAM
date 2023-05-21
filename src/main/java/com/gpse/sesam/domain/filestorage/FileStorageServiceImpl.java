package com.gpse.sesam.domain.filestorage;

import com.gpse.sesam.configuration.FileStorageConfiguration;
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

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageServiceImpl(final FileStorageConfiguration fileStorageConfiguration) {
        this.fileStorageLocation = Paths.get(fileStorageConfiguration.getBaseDir()).normalize();
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            throw new BeanCreationException("Could not initialize file storage", e);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName, e);
        }
    }

    @Override
    public String storeLogo(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path targetLocation = this.fileStorageLocation.resolve("Logo.svg");
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName, e);
        }
    }

    @Override
    public String storeFavicon(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path targetLocation = this.fileStorageLocation.resolve("Favicon.ico");
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName, e);
        }
    }

    @Override
    public void reset() {
        try (InputStream logoFile = Files.newInputStream(this.fileStorageLocation.resolve("T_logo_white.svg"));
             InputStream faviconFile = Files.newInputStream(this.fileStorageLocation.resolve("T_favicon.ico"))) {
            Path logoLocation = this.fileStorageLocation.resolve("Logo.svg");
            Path faviconLocation = this.fileStorageLocation.resolve("Favicon.ico");
            Files.copy(logoFile, logoLocation, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(faviconFile, faviconLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Could not reset resources", e);
        }
    }

}