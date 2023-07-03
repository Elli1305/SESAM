package com.gpse.sesam.domain.location.door.config.predefined;

import com.gpse.sesam.web.cmd.PredefinedConfigCmd;

import java.util.List;

public interface PredefinedConfigService {

    List<PredefinedConfig> getAllPreConfigs();

    PredefinedConfigCmd getPreConfig(Long id);

    void update(PredefinedConfigCmd predefinedConfig);

    void create(PredefinedConfigCmd predefinedConfig);

    void delete(Long id);
}
