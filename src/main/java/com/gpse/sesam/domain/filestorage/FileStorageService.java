package com.gpse.sesam.domain.filestorage;

import com.gpse.sesam.domain.colors.ColorTheme;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String storeFile(final MultipartFile file);

    String storeLogo(final MultipartFile file, String colorTheme);

    String storeFavicon(final MultipartFile file);

    void reset(final ColorTheme colorTheme);

}
