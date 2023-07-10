package com.gpse.sesam.domain.credential.credentials;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredential;
import com.gpse.sesam.domain.credential.credentials.internal.InternalCredential;
import com.gpse.sesam.domain.credential.issuing.FormEntry;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = InternalCredential.class, name = "internal"),
        @JsonSubTypes.Type(value = ExternalCredential.class, name = "external")
})
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
