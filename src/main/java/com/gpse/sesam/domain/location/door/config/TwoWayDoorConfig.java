package com.gpse.sesam.domain.location.door.config;

import com.gpse.sesam.domain.location.door.config.ProofConfig;
import jakarta.persistence.*;

@Entity
public class TwoWayDoorConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ProofConfig proofConfigIn;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ProofConfig proofConfigOut;

    @Column
    private String startTime;

    @Column
    private String endTime;

    @Column
    private boolean baseConfig;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProofConfig getProofConfigIn() {
        return proofConfigIn;
    }

    public void setProofConfigIn(ProofConfig proofConfigIn) {
        this.proofConfigIn = proofConfigIn;
    }

    public ProofConfig getProofConfigOut() {
        return proofConfigOut;
    }

    public void setProofConfigOut(ProofConfig proofConfigOut) {
        this.proofConfigOut = proofConfigOut;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isBaseConfig() {
        return baseConfig;
    }

    public void setBaseConfig(boolean baseConfig) {
        this.baseConfig = baseConfig;
    }
}
