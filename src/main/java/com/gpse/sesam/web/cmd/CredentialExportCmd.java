package com.gpse.sesam.web.cmd;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CredentialExportCmd {
    @NotNull
    private List<InternalCredentialExportCmd> internalCredentials;

    @NotNull
    private List<ExternalCredentialExportCmd> externalCredentials;

    public CredentialExportCmd() {
    }

    public CredentialExportCmd(List<InternalCredentialExportCmd> internalCredentials,
                               List<ExternalCredentialExportCmd> externalCredentials) {
        this.internalCredentials = internalCredentials;
        this.externalCredentials = externalCredentials;
    }

    public List<InternalCredentialExportCmd> getInternalCredentials() {
        return internalCredentials;
    }

    public void setInternalCredentials(List<InternalCredentialExportCmd> internalCredentials) {
        this.internalCredentials = internalCredentials;
    }

    public List<ExternalCredentialExportCmd> getExternalCredentials() {
        return externalCredentials;
    }

    public void setExternalCredentials(List<ExternalCredentialExportCmd> externalCredentials) {
        this.externalCredentials = externalCredentials;
    }
}
