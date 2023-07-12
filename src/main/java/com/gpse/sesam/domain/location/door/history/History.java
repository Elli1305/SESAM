package com.gpse.sesam.domain.location.door.history;

import com.gpse.sesam.domain.location.door.Door;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class History {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column
    private Date date;


    @OneToOne
    @JoinColumn(name = "door_id", nullable = false)
    private Door door;

    protected  History() {}
    public History(final Date date, final Door door) {
        this.date = date;
        this.door = door;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Door getDoor() {
        return door;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDoor(Door door) {
        this.door = door;
    }
}
