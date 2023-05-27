package com.gpse.sesam.domain.imprint;

import org.springframework.security.access.annotation.Secured;


public interface ImprintService {
    void createImprintEntry(String content);
    String getLatestImprintEntry();
    void deleteImprintEntry();
}
