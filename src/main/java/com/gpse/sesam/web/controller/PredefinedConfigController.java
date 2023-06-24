package com.gpse.sesam.web.controller;


import com.gpse.sesam.domain.location.door.config.predefined.PredefinedConfig;
import com.gpse.sesam.domain.location.door.config.predefined.PredefinedConfigService;
import com.gpse.sesam.web.cmd.PredefinedConfigCmd;
import com.gpse.sesam.web.exception.PredefinedConfigNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/preConfig")
@CrossOrigin
@Secured("EDITOR")
public class PredefinedConfigController {

    private final PredefinedConfigService predefinedConfigService;

    public PredefinedConfigController(PredefinedConfigService predefinedConfigService) {
        this.predefinedConfigService = predefinedConfigService;
    }


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody final PredefinedConfigCmd predefinedConfig) {
        predefinedConfigService.create(predefinedConfig);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody final PredefinedConfigCmd predefinedConfig) {
        predefinedConfigService.update(predefinedConfig);
    }

    @DeleteMapping("/delete/{id:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") final Long id) {
        predefinedConfigService.delete(id);
    }

    @GetMapping("/allConfigs")
    public List<PredefinedConfig> getAllConfigs() {
        return predefinedConfigService.getAllPreConfigs();
    }

    @GetMapping("/{id:\\d+}")
    public PredefinedConfig getConfig(@PathVariable("id") final Long id) {
        final Optional<PredefinedConfig> tempPreConfig = predefinedConfigService.getPreConfig(id);
        if (tempPreConfig.isPresent()) {
            return tempPreConfig.get();
        } else {
            throw new PredefinedConfigNotFoundException("PredefinedConfig not found with id: " + id);
        }
    }
}
