package com.gpse.sesam.domain.location.door.config.predefined;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PredefinedConfigServiceImpl implements PredefinedConfigService {

    private final PredefinedConfigRepository predefinedConfigRepository;

    @Autowired
    public PredefinedConfigServiceImpl(final PredefinedConfigRepository predefinedConfigRepository) {
        this.predefinedConfigRepository = predefinedConfigRepository;
    }

    @Override
    public List<PredefinedConfig> getAllPreConfigs() {
        final List<PredefinedConfig> preConfigs = new ArrayList<>();

        predefinedConfigRepository.findAll().forEach(preConfigs::add);

        return preConfigs;
    }

    @Override
    public Optional<PredefinedConfig> getPreConfig(final Long id) {
        return predefinedConfigRepository.findById(id);
    }

    @Override
    public void update(PredefinedConfig predefinedConfig) {
        final Optional<PredefinedConfig> tempPreConfig = getPreConfig(predefinedConfig.getId());
        if (tempPreConfig.isPresent()) {
            tempPreConfig.get().setDoorIn(predefinedConfig.getDoorIn());
            tempPreConfig.get().setDoorOut(predefinedConfig.getDoorOut());
            predefinedConfigRepository.save(tempPreConfig.get());
        }
    }

    @Override
    public void create(PredefinedConfig predefinedConfig) {
        predefinedConfigRepository.save(predefinedConfig);
    }

    @Override
    public void delete(Long id) {
        predefinedConfigRepository.deleteById(id);
    }

}
