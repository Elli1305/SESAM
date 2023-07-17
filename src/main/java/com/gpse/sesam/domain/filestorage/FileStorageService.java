package com.gpse.sesam.domain.filestorage;

import com.gpse.sesam.domain.colors.ColorTheme;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String storeFile(MultipartFile file);

    String storeLogo(MultipartFile file, String colorTheme);

    String storeFavicon(MultipartFile file);

    void reset(ColorTheme colorTheme);
}
