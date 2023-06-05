package com.gpse.sesam.domain.credential.issuing;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record IssueCredentialRequest(@NotEmpty String agent, @Valid IssueCredential credential) {
}
