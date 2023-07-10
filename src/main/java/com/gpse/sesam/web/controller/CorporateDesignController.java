package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.colors.ColorTheme;
import com.gpse.sesam.domain.colors.Colors;
import com.gpse.sesam.domain.colors.ColorsService;
import com.gpse.sesam.domain.filestorage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
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

    @PostMapping(path = "/save/logo-light")
    @Secured("ADMINISTRATOR")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveLogoLight(@RequestPart("file") MultipartFile file) {

        fileStorageService.storeLogo(file, "LIGHT");

    }

    @PostMapping(path = "/save/logo-dark")
    @Secured("ADMINISTRATOR")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveLogoDark(@RequestPart("file") MultipartFile file) {

        fileStorageService.storeLogo(file, "DARK");

    }

    @PostMapping(path = "/save/favicon")
    @Secured("ADMINISTRATOR")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveFavicon(@RequestPart("file") MultipartFile file) {

        fileStorageService.storeFavicon(file);

    }

    @PostMapping(path = "/save/colors/{colorTheme}")
    @Secured("ADMINISTRATOR")
    @ResponseStatus(HttpStatus.CREATED)
    public Colors changeColors(@PathVariable String colorTheme, @RequestBody Colors colors) {

        return colorsService.changeColors(colorTheme.equals("LIGHT") ? ColorTheme.LIGHT : ColorTheme.DARK, colors);

    }

    @GetMapping(path = "/get/colors/{colorTheme}")
    @ResponseStatus(HttpStatus.OK)
    public Colors getColors(@PathVariable String colorTheme) {

        return colorsService.getColors(colorTheme.equals("LIGHT") ? ColorTheme.LIGHT : ColorTheme.DARK);

    }

    @PostMapping(path = "/reset/{colorTheme}")
    @Secured("ADMINISTRATOR")
    @ResponseStatus(HttpStatus.CREATED)
    public Colors reset(@PathVariable String colorTheme) {

        colorsService.resetColors(colorTheme.equals("LIGHT") ? ColorTheme.LIGHT : ColorTheme.DARK);
        fileStorageService.reset(colorTheme.equals("LIGHT") ? ColorTheme.LIGHT : ColorTheme.DARK);

        return getColors(colorTheme);
    }

}
