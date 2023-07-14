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

    @Column
    private ColorTheme theme;

    @Column
    private String logoPath;

    @Column(nullable = false)
    private String bgC;
    @Column(nullable = false)
    private String textC;

    @Column(nullable = false)
    private String primaryColor;
    @Column(nullable = false)
    private String secondary;
    @Column(nullable = false)
    private String accent;
    @Column(nullable = false)
    private String dark;
    @Column(nullable = false)
    private String light;
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

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDefaultColors() {
        return defaultColors;
    }

    public void setDefaultColors(boolean defaultColors) {
        this.defaultColors = defaultColors;
    }

    public ColorTheme getTheme() {
        return theme;
    }

    public void setTheme(ColorTheme theme) {
        this.theme = theme;
    }

    public String getBgC() {
        return bgC;
    }

    public void setBgC(String bgC) {
        this.bgC = bgC;
    }

    public String getTextC() {
        return textC;
    }

    public void setTextC(String textC) {
        this.textC = textC;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public String getAccent() {
        return accent;
    }

    public void setAccent(String accent) {
        this.accent = accent;
    }

    public String getDark() {
        return dark;
    }

    public void setDark(String dark) {
        this.dark = dark;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    public String getNegative() {
        return negative;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }
}
