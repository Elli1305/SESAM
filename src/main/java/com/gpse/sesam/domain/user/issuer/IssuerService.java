package com.gpse.sesam.domain.user.issuer;

import com.gpse.sesam.web.cmd.IssuerResponseCmd;

import java.util.List;
import java.util.Optional;


public interface IssuerService {

    List<Issuer> getIssuers();

    Issuer getIssuerByMail(String username);

    Optional<Issuer> getIssuer(Long id);

    void deleteIssuer(Issuer issuer);

    void saveAll(Iterable<Issuer> issuer);

    public void updateIssuer(Long id, IssuerResponseCmd cmd);
}
