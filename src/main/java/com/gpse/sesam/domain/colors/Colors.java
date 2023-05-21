package com.gpse.sesam.domain.colors;

import jakarta.persistence.*;

@Entity
public class Colors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private boolean defaultColors;

    @Column(nullable = false)
    private String primaryColor;
    @Column(nullable = false)
    private String secondary;
    @Column(nullable = false)
    private String accent;
    @Column(nullable = false)
    private String dark;
    @Column(nullable = false)
    private String lightBlue;
    @Column(nullable = false)
    private String positive;
    @Column(nullable = false)
    private String negative;
    @Column(nullable = false)
    private String info;
    @Column(nullable = false)
    private String warning;

    public Colors() {

    }

    public Long getId() {
        return id;
    }

    public boolean isDefaultColors() {
        return defaultColors;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public String getSecondary() {
        return secondary;
    }

    public String getAccent() {
        return accent;
    }

    public String getDark() {
        return dark;
    }

    public String getLightBlue() {
        return lightBlue;
    }

    public String getPositive() {
        return positive;
    }

    public String getNegative() {
        return negative;
    }

    public String getInfo() {
        return info;
    }

    public String getWarning() {
        return warning;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDefaultColors(boolean defaultColors) {
        this.defaultColors = defaultColors;
    }

    public void setPrimaryColor(String primary) {
        this.primaryColor = primary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public void setAccent(String accent) {
        this.accent = accent;
    }

    public void setDark(String dark) {
        this.dark = dark;
    }

    public void setLightBlue(String lightBlue) {
        this.lightBlue = lightBlue;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
}
