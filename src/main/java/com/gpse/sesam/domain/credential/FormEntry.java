package com.gpse.sesam.domain.credential;

import jakarta.persistence.*;

@Entity
public class FormEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private FormEntryType type;

    @Column(nullable = false)
    private String attributeName;

    protected FormEntry() {
    }

    public FormEntry(String label, FormEntryType type, String attributeName) {
        this.label = label;
        this.type = type;
        this.attributeName = attributeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public FormEntryType getType() {
        return type;
    }

    public void setType(FormEntryType type) {
        this.type = type;
    }

    public String getAttributeName() {
        return attributeName;
    }
}
