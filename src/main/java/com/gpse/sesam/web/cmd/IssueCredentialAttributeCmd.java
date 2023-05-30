package com.gpse.sesam.web.cmd;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IssueCredentialAttributeCmd(@NotNull Long id, @NotNull @NotBlank String value) {
}
