package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.filestorage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/corpdesign")
public class FileStorageController {

    FileStorageService fileStorageService;

    @Autowired
    public FileStorageController(final FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(path = "/save/logo")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveLogo(@RequestPart("file") MultipartFile file) {

        fileStorageService.storeLogo(file);

    }

    @PostMapping(path = "/save/favicon")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveFavicon(@RequestPart("file") MultipartFile file) {

        fileStorageService.storeFavicon(file);

    }

}
