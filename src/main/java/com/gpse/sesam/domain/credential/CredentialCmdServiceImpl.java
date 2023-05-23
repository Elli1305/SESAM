package com.gpse.sesam.domain.credential;

import com.gpse.sesam.web.cmd.CredentialCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredentialCmdServiceImpl implements CredentialCmdService{

    private final CredentialCmdRepository credentialCmdRepository;

    @Autowired
    public CredentialCmdServiceImpl(CredentialCmdRepository credentialCmdRepository) {
        this.credentialCmdRepository = credentialCmdRepository;
    }

    @Override
    public void deleteAll() {
        credentialCmdRepository.deleteAll();
    }

    @Override
    public void saveAll(Iterable<CredentialCmd> credentials) {
        credentialCmdRepository.saveAll(credentials);
    }
}
