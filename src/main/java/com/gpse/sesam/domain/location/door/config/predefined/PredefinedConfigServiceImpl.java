package com.gpse.sesam.domain.location.door.config.predefined;

import com.gpse.sesam.domain.location.door.config.ProofConfig;
import com.gpse.sesam.util.ConfigCmdMapper;
import com.gpse.sesam.web.cmd.PredefinedConfigCmd;
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
    public void update(PredefinedConfigCmd predefinedConfig) {
        final Optional<PredefinedConfig> tempPreConfig = getPreConfig(predefinedConfig.getId());
        final ProofConfig configIn = ConfigCmdMapper.fromCmd(predefinedConfig.getDoorConfigIn());
        final ProofConfig configOut = ConfigCmdMapper.fromCmd(predefinedConfig.getDoorConfigOut());
        if (tempPreConfig.isPresent()) {
            tempPreConfig.get().setName(predefinedConfig.getName());
            tempPreConfig.get().setDoorIn(configIn);
            tempPreConfig.get().setDoorOut(configOut);
            predefinedConfigRepository.save(tempPreConfig.get());
        }
    }

    @Override
    public void create(PredefinedConfigCmd predefinedConfig) {
        PredefinedConfig config = new PredefinedConfig();
        config.setName(predefinedConfig.getName());
        config.setDoorIn(ConfigCmdMapper.fromCmd(predefinedConfig.getDoorConfigIn()));
        config.setDoorOut(ConfigCmdMapper.fromCmd(predefinedConfig.getDoorConfigOut()));
        predefinedConfigRepository.save(config);
    }

    @Override
    public void delete(Long id) {
        predefinedConfigRepository.deleteById(id);
    }

}
