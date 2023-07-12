package com.gpse.sesam.domain.location.door.config.predefined;

import com.gpse.sesam.domain.credential.credentials.internal.CredentialService;
import com.gpse.sesam.domain.location.door.config.ProofConfig;
import com.gpse.sesam.domain.location.door.config.TwoWayDoorConfig;
import com.gpse.sesam.util.ConfigCmdMapper;
import com.gpse.sesam.web.cmd.DoorConfigCmd;
import com.gpse.sesam.web.cmd.PredefinedConfigCmd;
import com.gpse.sesam.web.cmd.TwoWayDoorConfigCmd;
import com.gpse.sesam.web.exception.InvalidDoorConfiguration;
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
            List<TwoWayDoorConfigCmd> doorConfig = new ArrayList<>();
            for (int i = 0; i < predefinedConfig.getDoorConfig().size(); i++) {
                TwoWayDoorConfigCmd oneConfig = new TwoWayDoorConfigCmd();
                oneConfig.setId(predefinedConfig.getDoorConfig().get(i).getId());
                oneConfig.setBaseConfig(predefinedConfig.getDoorConfig().get(i).isBaseConfig());
                oneConfig.setStartTime(predefinedConfig.getDoorConfig().get(i).getStartTime());
                oneConfig.setEndTime(predefinedConfig.getDoorConfig().get(i).getEndTime());
                DoorConfigCmd in = configCmdMapper.toCmd(predefinedConfig.getDoorConfig().get(i).getProofConfigIn());
                DoorConfigCmd out = configCmdMapper.toCmd(predefinedConfig.getDoorConfig().get(i).getProofConfigOut());
                oneConfig.setDoorConfigIn(in);
                oneConfig.setDoorConfigOut(out);
                doorConfig.add(oneConfig);
            }
            return new PredefinedConfigCmd(predefinedConfig.getId(), predefinedConfig.getName(), doorConfig);
        } catch (ParseException e) {
            throw new InvalidDoorConfiguration("door config could not be parsed", e);
        }
    }

    @Override
    public void update(PredefinedConfigCmd predefinedConfig) {
        final Optional<PredefinedConfig> tempPreConfig = predefinedConfigRepository.findById(predefinedConfig.getId());
        final List<TwoWayDoorConfig> twoWayConfigs = new ArrayList<>();

        setupTwoWayDoorConfigsForSave(predefinedConfig, twoWayConfigs);
        if (tempPreConfig.isPresent()) {
            tempPreConfig.get().setName(predefinedConfig.getName());
            tempPreConfig.get().setDoorConfig(twoWayConfigs);
            predefinedConfigRepository.save(tempPreConfig.get());
        }
    }

    @Override
    public void create(PredefinedConfigCmd predefinedConfig) {
        PredefinedConfig config = new PredefinedConfig();
        List<TwoWayDoorConfig> twoWayConfigs = new ArrayList<>();

        setupTwoWayDoorConfigsForSave(predefinedConfig, twoWayConfigs);
        config.setName(predefinedConfig.getName());
        config.setDoorConfig(twoWayConfigs);

        predefinedConfigRepository.save(config);
    }

    private void setupTwoWayDoorConfigsForSave(PredefinedConfigCmd predefinedConfig,
                                               List<TwoWayDoorConfig> twoWayConfigs) {
        for (int i = 0; i < predefinedConfig.getDoorConfig().size(); i++) {
            TwoWayDoorConfig tempConfig = new TwoWayDoorConfig();
            tempConfig.setBaseConfig(predefinedConfig.getDoorConfig().get(i).isBaseConfig());
            tempConfig.setStartTime(predefinedConfig.getDoorConfig().get(i).getStartTime());
            tempConfig.setEndTime(predefinedConfig.getDoorConfig().get(i).getEndTime());
            final ProofConfig configIn = ConfigCmdMapper.
                    fromCmd(predefinedConfig.getDoorConfig().get(i).getDoorConfigIn());
            final ProofConfig configOut = ConfigCmdMapper.
                    fromCmd(predefinedConfig.getDoorConfig().get(i).getDoorConfigOut());
            tempConfig.setProofConfigIn(configIn);
            tempConfig.setProofConfigOut(configOut);
            twoWayConfigs.add(tempConfig);
        }
    }

    @Override
    public void delete(Long id) {
        predefinedConfigRepository.deleteById(id);
    }

}
