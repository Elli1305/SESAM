package com.gpse.sesam.domain.credential.credentials;

import com.gpse.sesam.domain.credential.issuing.FormEntry;

import java.util.List;

public interface Credential {

    Long getId();

    void setId(final Long id);

    String getName();

    void setName(final String name);

    String getCredentialDefinitionId();

    void setCredentialDefinitionId(final String credentialDefinitionId);

    List<FormEntry> getForm();

    void setForm(List<FormEntry> form);

}
