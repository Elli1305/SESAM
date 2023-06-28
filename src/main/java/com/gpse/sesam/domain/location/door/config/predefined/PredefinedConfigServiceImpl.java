package com.gpse.sesam.domain.location.door.config.predefined;

import com.gpse.sesam.domain.credential.credentials.CredentialService;
import com.gpse.sesam.domain.location.door.config.ProofConfig;
import com.gpse.sesam.util.ConfigCmdMapper;
import com.gpse.sesam.web.cmd.DoorConfigCmd;
import com.gpse.sesam.web.cmd.PredefinedConfigCmd;
import com.gpse.sesam.web.exception.PredefinedConfigNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PredefinedConfigServiceImpl implements PredefinedConfigService {

    private final PredefinedConfigRepository predefinedConfigRepository;

    private final CredentialService credentialService;

    private final ConfigCmdMapper configCmdMapper;

    @Autowired
    public PredefinedConfigServiceImpl(final PredefinedConfigRepository predefinedConfigRepository,
                                       CredentialService credentialService) {
        this.predefinedConfigRepository = predefinedConfigRepository;
        this.credentialService = credentialService;
        this.configCmdMapper = new ConfigCmdMapper(credentialService);
    }

    @Override
    public List<PredefinedConfig> getAllPreConfigs() {
        final List<PredefinedConfig> preConfigs = new ArrayList<>();

        predefinedConfigRepository.findAll().forEach(preConfigs::add);

        return preConfigs;
    }

    @Override
    public PredefinedConfigCmd getPreConfig(final Long id) {
        PredefinedConfig predefinedConfig = predefinedConfigRepository.findById(id).orElseThrow(()
                -> new PredefinedConfigNotFoundException("PredefinedConfig with id: " + id + " not found"));
        try {
            DoorConfigCmd in = configCmdMapper.toCmd(predefinedConfig.getDoorIn());
            DoorConfigCmd out = configCmdMapper.toCmd(predefinedConfig.getDoorOut());
            PredefinedConfigCmd predefinedConfigCmd = new PredefinedConfigCmd(predefinedConfig.getId(),
                    predefinedConfig.getName(), in, out);
            return predefinedConfigCmd;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PredefinedConfigCmd predefinedConfig) {
        final Optional<PredefinedConfig> tempPreConfig = predefinedConfigRepository.findById(predefinedConfig.getId());
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
