package com.gpse.sesam.domain.location.door.config.predefined;


import com.gpse.sesam.domain.location.door.config.TwoWayDoorConfig;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class PredefinedConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<TwoWayDoorConfig> doorConfig;

    public PredefinedConfig(String name, List<TwoWayDoorConfig> twoWayDoorConfig) {
        this.name = name;
        this.doorConfig = twoWayDoorConfig;
    }

    protected PredefinedConfig() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TwoWayDoorConfig> getDoorConfig() {
        return doorConfig;
    }

    public void setDoorConfig(List<TwoWayDoorConfig> doorConfig) {
        this.doorConfig = doorConfig;
    }
}
