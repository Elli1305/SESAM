package com.gpse.sesam.domain.credential;

import java.util.List;

public record IssueCredential(String credentialDefinitionId, List<IssueCredentialAttribute> attributes) {
}