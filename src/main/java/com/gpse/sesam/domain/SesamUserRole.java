package com.gpse.sesam.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serial;

@Entity
public class SesamUserRole {
    @Serial
    private static final long serialVersionUID = 43L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @JsonIgnore
    private Long id;

    @Column(nullable = false)
    private AttainableRole role;

    @Column(nullable = false)
    private boolean granted;

    protected SesamUserRole() {
    }

    public SesamUserRole(AttainableRole role) {
        this.role = role;
        this.granted = false;
    }

    public AttainableRole getRole() {
        return role;
    }

    public void setRole(AttainableRole role) {
        this.role = role;
    }

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public enum AttainableRole {
        ADMINISTRATOR,
        EDITOR,
        ISSUER
    }
}
