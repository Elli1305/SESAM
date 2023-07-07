package com.gpse.sesam.web.cmd;

import java.util.List;

public class ExternalCredentialCmd {
    private String categoryName;

    private String credentialName;

    private List<String> internalCredential;

    public ExternalCredentialCmd(String categoryName, String credentialName, List<String> internalCredential) {
        this.categoryName = categoryName;
        this.credentialName = credentialName;
        this.internalCredential = internalCredential;
    }

    public List<String> getInternalCredential() {
        return internalCredential;
    }

    public String getCredentialName() {
        return credentialName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCredentialName(String credentialName) {
        this.credentialName = credentialName;
    }

    public void setInternalCredential(List<String> internalCredential) {
        this.internalCredential = internalCredential;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
