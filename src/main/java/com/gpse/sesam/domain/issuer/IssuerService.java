package com.gpse.sesam.domain.issuer;

import com.gpse.sesam.domain.user.Issuer;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;


public interface IssuerService {

    List<Issuer> getIssuers();

    Issuer getIssuerByMail(String username);



    void deleteIssuer(Issuer issuer);

    void saveAll(Iterable<Issuer> issuer);





}
