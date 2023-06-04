package com.gpse.sesam.domain.user.issuer;

import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.domain.credential.credentials.CredentialRepository;
import com.gpse.sesam.domain.location.room.Room;
import com.gpse.sesam.domain.location.room.RoomRepository;
import com.gpse.sesam.web.cmd.IssuerResponseCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class IssuerServiceImpl implements IssuerService {
    private final IssuerRepository issuerRepository;
    private final RoomRepository roomRepository;
    private final CredentialRepository credentialRepository;

    @Autowired
    public IssuerServiceImpl(IssuerRepository issuerRepository,
                             RoomRepository roomRepository, CredentialRepository credentialRepository) {
        this.issuerRepository = issuerRepository;
        this.roomRepository = roomRepository;
        this.credentialRepository = credentialRepository;
    }



    @Override
    public List<Issuer> getIssuers() {
        final List<Issuer> issuers = new ArrayList<>();
        issuerRepository.findAll().forEach(issuers::add);
        return issuers;
    }

    @Override
    public Issuer getIssuerByMail(String email) {
        return issuerRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Optional<Issuer> getIssuer(Long id) {
        return issuerRepository.findById(String.valueOf(id));
    }


    @Override
    public void deleteIssuer(Issuer issuer) {
        issuerRepository.delete(issuer);
    }

    @Override
    public void saveAll(Iterable<Issuer> issuers) {
        issuerRepository.saveAll(issuers);
    }

    @Override
    public void updateIssuer(Long id, IssuerResponseCmd cmd) {
        Optional<Issuer> issuer = getIssuer(id);
        List<Credential> credentials = new ArrayList<>();
        if (issuer.isPresent()) {
            Optional<Room> room = roomRepository.findById(cmd.getRoom());
            if (room.isPresent()) {
                issuer.get().setRoom(room.get());
            }
            for (Long cred: cmd.getCredentials()) {
                Optional<Credential> credential = credentialRepository.findById(cred);
                if (credential.isPresent()) {
                    credentials.add(credential.get());
                }
            }
            issuer.get().setCredentials(credentials);
        }
    }
}
