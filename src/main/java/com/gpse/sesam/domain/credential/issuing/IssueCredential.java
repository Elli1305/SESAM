package com.gpse.sesam.domain.credential.issuing;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record IssueCredential(
        @NotEmpty String credentialDefinitionId,
        @Valid List<IssueCredentialAttribute> attributes
) {
}