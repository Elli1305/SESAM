package com.gpse.sesam.domain.colors;

import jakarta.persistence.Access;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorsServiceImpl implements ColorsService {

    private final ColorsRepository colorsRepository;

    public ColorsServiceImpl(ColorsRepository colorsRepository) {
        this.colorsRepository = colorsRepository;
    }

    @Override
    public Colors getColors(ColorTheme colorTheme) {
        return colorsRepository.findByDefaultColorsIsFalseAndTheme(colorTheme);
    }

    @Override
    public Colors getDefaultColors(ColorTheme colorTheme) {
        return colorsRepository.findByDefaultColorsIsTrueAndTheme(colorTheme);
    }

    @Override
    public Colors changeColors(ColorTheme colorTheme, Colors colors) {
        Long currentColorsId = colorsRepository.findByDefaultColorsIsFalseAndTheme(colorTheme).getId();
        colors.setId(currentColorsId);
        colorsRepository.save(colors);
        return colors;
    }

    @Override
    public void resetColors(ColorTheme colorTheme) {
        Long currentColorsId = colorsRepository.findByDefaultColorsIsFalseAndTheme(colorTheme).getId();
        Colors defaultColors = colorsRepository.findByDefaultColorsIsTrueAndTheme(colorTheme);
        Colors colors = new Colors();
        colors.setId(currentColorsId);
        colors.setDefaultColors(false);
        colors.setTheme(defaultColors.getTheme());
        colors.setLogoPath(defaultColors.getLogoPath());
        colors.setBgC(defaultColors.getBgC());
        colors.setTextC(defaultColors.getTextC());
        colors.setPrimaryColor(defaultColors.getPrimaryColor());
        colors.setSecondary(defaultColors.getSecondary());
        colors.setAccent(defaultColors.getAccent());
        colors.setDark(defaultColors.getDark());
        colors.setLight(defaultColors.getLight());
        colors.setPositive(defaultColors.getPositive());
        colors.setNegative(defaultColors.getNegative());
        colors.setInfo(defaultColors.getInfo());
        colors.setWarning(defaultColors.getWarning());
        colorsRepository.save(colors);
    }

    @Override
    public void saveAll(List<Colors> defaultColors) {
        colorsRepository.saveAll(defaultColors);
    }

    @Override
    public void deleteAll() {
        colorsRepository.deleteAll();
    }

}
