package com.gpse.sesam.domain.credential;

import jakarta.persistence.*;

@Entity
public class ChecklistEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String label;

    protected ChecklistEntry() {

    }

    public ChecklistEntry(String label) {
        this.label = label;
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
}
