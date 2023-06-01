package com.gpse.sesam.web.cmd;

import com.gpse.sesam.domain.credential.Category;
import com.gpse.sesam.domain.credential.ChecklistEntry;
import com.gpse.sesam.domain.credential.FormEntry;
import com.gpse.sesam.domain.user.Issuer;

import java.util.List;

public class CredentialForMappingCmd {
    private Long id;

    private String name;

    private String credentialDefinitionId;
    private String credentialName;

    private String agent;

    private Category category;

    private List<ChecklistEntry> checklistEntryList;

    private List<FormEntry> formEntries;

    private List<Issuer> issuers;


    protected CredentialForMappingCmd() { }
    public CredentialForMappingCmd(Long id, String name, String credentialDefinitionId, String agent,
                                   List<ChecklistEntry> checklistEntryList, List<FormEntry> formEntries, List<Issuer> issuers) {
        this.id = id;
        this.agent = agent;
        this.name = name;
        this.credentialDefinitionId = credentialDefinitionId;
        this.checklistEntryList = checklistEntryList;
        this.formEntries = formEntries;
        this.issuers = issuers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCredentialName(String credentialName) {
        this.credentialName = credentialName;
    }

    public String getCredentialName() {
        return credentialName;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setCredentialDefinitionId(String credentialDefinitionId) {
        this.credentialDefinitionId = credentialDefinitionId;
    }

    public String getCredentialDefinitionId() {
        return credentialDefinitionId;
    }

    public List<ChecklistEntry> getChecklistEntryList() {
        return checklistEntryList;
    }

    public List<FormEntry> getFormEntries() {
        return formEntries;
    }

    public List<Issuer> getIssuers() {
        return issuers;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public void setChecklistEntryList(List<ChecklistEntry> checklistEntryList) {
        this.checklistEntryList = checklistEntryList;
    }

    public void setFormEntries(List<FormEntry> formEntries) {
        this.formEntries = formEntries;
    }

    public void setIssuers(List<Issuer> issuers) {
        this.issuers = issuers;
    }
}
