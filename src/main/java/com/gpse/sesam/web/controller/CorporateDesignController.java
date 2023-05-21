package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.colors.Colors;
import com.gpse.sesam.domain.colors.ColorsService;
import com.gpse.sesam.domain.filestorage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/corpdesign")
public class CorporateDesignController {

    private final FileStorageService fileStorageService;
    private final ColorsService colorsService;

    @Autowired
    public CorporateDesignController(final FileStorageService fileStorageService, final ColorsService colorsService) {
        this.fileStorageService = fileStorageService;
        this.colorsService = colorsService;
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

    @PostMapping(path = "/save/colors")
    @ResponseStatus(HttpStatus.CREATED)
    public void changeColors(@RequestBody Colors colors) {

        colorsService.changeColors(colors);

    }

    @GetMapping(path = "/get/colors")
    @ResponseStatus(HttpStatus.OK)
    public Colors getColors() {

        return colorsService.getColors();

    }

    @PostMapping(path = "/reset")
    @ResponseStatus(HttpStatus.CREATED)
    public void reset() {

        fileStorageService.reset();
        colorsService.resetColors();

    }

}
