package com.gpse.sesam.domain.location;

import jakarta.persistence.*;

@Entity
public class Door {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;


    //Field/List for Credentials?

    protected Door() {

    }

    public Door(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
