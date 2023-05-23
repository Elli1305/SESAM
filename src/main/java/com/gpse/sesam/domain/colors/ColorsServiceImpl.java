package com.gpse.sesam.domain.colors;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorsServiceImpl implements ColorsService {

    private final ColorsRepository colorsRepository;

    public ColorsServiceImpl(ColorsRepository colorsRepository) {
        this.colorsRepository = colorsRepository;
    }

    @Override
    public Colors getColors() {
        return colorsRepository.findByDefaultColorsIsFalse();
    }

    @Override
    public Colors changeColors(Colors colors) {
        Long currentColorsId = colorsRepository.findByDefaultColorsIsFalse().getId();
        colors.setId(currentColorsId);
        colorsRepository.save(colors);
        return colors;
    }

    @Override
    public void resetColors() {
        Long currentColorsId = colorsRepository.findByDefaultColorsIsFalse().getId();
        Colors defaultColors = colorsRepository.findByDefaultColorsIsTrue();
        Colors colors = new Colors();
        colors.setId(currentColorsId);
        colors.setDefaultColors(false);
        colors.setBgC(defaultColors.getBgC());
        colors.setTextC(defaultColors.getTextC());
        colors.setPrimaryColor(defaultColors.getPrimaryColor());
        colors.setSecondary(defaultColors.getSecondary());
        colors.setAccent(defaultColors.getAccent());
        colors.setDark(defaultColors.getDark());
        colors.setLightBlue(defaultColors.getLightBlue());
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
