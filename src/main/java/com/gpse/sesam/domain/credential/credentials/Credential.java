package com.gpse.sesam.domain.credential.credentials;

import com.gpse.sesam.domain.credential.issuing.FormEntry;

import java.util.List;

public interface Credential {

    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    String getCredentialDefinitionId();

    void setCredentialDefinitionId(String credentialDefinitionId);

    List<FormEntry> getForm();

    void setForm(List<FormEntry> form);

}
