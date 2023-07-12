package com.gpse.sesam.web.cmd;

public class CredentialSchemaErrorCmd {
    private String code;

    public CredentialSchemaErrorCmd(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
