package com.gpse.sesam.domain.location.door.config.predefined;


import com.gpse.sesam.domain.location.door.config.ProofConfig;
import jakarta.persistence.*;

@Entity
public class PredefinedConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private ProofConfig doorIn;

    @OneToOne(cascade = CascadeType.ALL)
    private ProofConfig doorOut;

    public PredefinedConfig(String name, ProofConfig in, ProofConfig out) {
        this.name = name;
        this.doorIn = in;
        this.doorOut = out;
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

    public ProofConfig getDoorIn() {
        return doorIn;
    }

    public void setDoorIn(ProofConfig doorIn) {
        this.doorIn = doorIn;
    }

    public ProofConfig getDoorOut() {
        return doorOut;
    }

    public void setDoorOut(ProofConfig doorOut) {
        this.doorOut = doorOut;
    }
}
