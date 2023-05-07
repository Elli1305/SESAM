package com.gpse.sesam.domain.credential;

import jakarta.persistence.*;

@Entity
public class FormEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, unique = true)
    private String label;

    @Column(nullable = false)
    private FormEntryType type;

    protected FormEntry (){}
    public FormEntry (Long id, String label, FormEntryType type) {
        this.id = id;
        this.label = label;
        this.type = type;
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
}
