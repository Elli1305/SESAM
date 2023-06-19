package com.gpse.sesam.domain.location.door.config.predefined;

import java.util.List;
import java.util.Optional;

public interface PredefinedConfigService {

    List<PredefinedConfig> getAllPreConfigs();

    Optional<PredefinedConfig> getPreConfig(Long id);

    void update(PredefinedConfig predefinedConfig);

    void create(PredefinedConfig predefinedConfig);

    void delete(Long id);
}
