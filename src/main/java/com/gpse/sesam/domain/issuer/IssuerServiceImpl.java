package com.gpse.sesam.domain.issuer;

import com.gpse.sesam.domain.user.Issuer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class IssuerServiceImpl implements IssuerService {
    private final IssuerRepository issuerRepository;

    @Autowired
    public IssuerServiceImpl(IssuerRepository issuerRepository) {
        this.issuerRepository = issuerRepository;
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
    public void deleteIssuer(Issuer issuer) {
        issuerRepository.delete(issuer);
    }

    @Override
    public void saveAll(Iterable<Issuer> issuers) {
        issuerRepository.saveAll(issuers);
    }


}
