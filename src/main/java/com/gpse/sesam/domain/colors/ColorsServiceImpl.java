package com.gpse.sesam.domain.colors;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Diese Service-Klasse ist für die Verwaltung von Farben und deren Konfigurationen zuständig.
 */
@Service
public class ColorsServiceImpl implements ColorsService {

    private final ColorsRepository colorsRepository;


    /**
     * Erzeugt eine neue ColorsServiceImpl-Instanz mit dem angegebenen ColorsRepository.
     *
     * @param colorsRepository das Repository für Farbdaten
     */

    public ColorsServiceImpl(final ColorsRepository colorsRepository) {
        this.colorsRepository = colorsRepository;
    }

    /**
     * Ruft die Farbkonfiguration für das angegebene Farbthema ab.
     *
     * @param colorTheme das Farbthema, für das die Farben abgerufen werden sollen
     * @return die Farbkonfiguration für das angegebene Farbthema
     */
    @Override
    public Colors getColors(final ColorTheme colorTheme) {
        return colorsRepository.findByDefaultColorsIsFalseAndTheme(colorTheme);
    }

    @Override
    public Colors getDefaultColors(final ColorTheme colorTheme) {
        return colorsRepository.findByDefaultColorsIsTrueAndTheme(colorTheme);
    }


    /**
     * Ändert die Farbkonfiguration für das angegebene Farbthema.
     *
     * @param colorTheme das Farbthema, für das die Farben geändert werden sollen
     * @param colors     die neue Farbkonfiguration
     * @return die aktualisierte Farbkonfiguration
     */
    @Override
    public Colors changeColors(final ColorTheme colorTheme, final Colors colors) {
        Long currentColorsId = colorsRepository.findByDefaultColorsIsFalseAndTheme(colorTheme).getId();
        colors.setId(currentColorsId);
        colorsRepository.save(colors);
        return colors;
    }

    /**
     * Setzt die Farbkonfiguration für das angegebene Farbthema auf die Standardfarben zurück.
     *
     * @param colorTheme das Farbthema, für das die Farben zurückgesetzt werden sollen
     */
    @Override
    public void resetColors(final ColorTheme colorTheme) {
        Long currentColorsId = colorsRepository.findByDefaultColorsIsFalseAndTheme(colorTheme).getId();
        Colors defaultColors = colorsRepository.findByDefaultColorsIsTrueAndTheme(colorTheme);
        final Colors colors = new Colors();
        colors.setId(currentColorsId);
        colors.setDefaultColors(false);
        colors.setCorporateName(defaultColors.getCorporateName());
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
    public void resetName() {
        Colors currentColorsLight = colorsRepository.findByDefaultColorsIsFalseAndTheme(ColorTheme.LIGHT);
        Colors currentColorsDark = colorsRepository.findByDefaultColorsIsFalseAndTheme(ColorTheme.DARK);
        String defaultName = colorsRepository.findByDefaultColorsIsTrueAndTheme(ColorTheme.LIGHT).getCorporateName();
        final Colors colorsLight = new Colors();
        colorsLight.setId(currentColorsLight.getId());
        colorsLight.setDefaultColors(false);
        colorsLight.setCorporateName(defaultName);
        colorsLight.setTheme(currentColorsLight.getTheme());
        colorsLight.setLogoPath(currentColorsLight.getLogoPath());
        colorsLight.setBgC(currentColorsLight.getBgC());
        colorsLight.setTextC(currentColorsLight.getTextC());
        colorsLight.setPrimaryColor(currentColorsLight.getPrimaryColor());
        colorsLight.setSecondary(currentColorsLight.getSecondary());
        colorsLight.setAccent(currentColorsLight.getAccent());
        colorsLight.setDark(currentColorsLight.getDark());
        colorsLight.setLight(currentColorsLight.getLight());
        colorsLight.setPositive(currentColorsLight.getPositive());
        colorsLight.setNegative(currentColorsLight.getNegative());
        colorsLight.setInfo(currentColorsLight.getInfo());
        colorsLight.setWarning(currentColorsLight.getWarning());
        final Colors colorsDark = new Colors();
        colorsDark.setId(currentColorsDark.getId());
        colorsDark.setDefaultColors(false);
        colorsDark.setCorporateName(defaultName);
        colorsDark.setTheme(currentColorsDark.getTheme());
        colorsDark.setLogoPath(currentColorsDark.getLogoPath());
        colorsDark.setBgC(currentColorsDark.getBgC());
        colorsDark.setTextC(currentColorsDark.getTextC());
        colorsDark.setPrimaryColor(currentColorsDark.getPrimaryColor());
        colorsDark.setSecondary(currentColorsDark.getSecondary());
        colorsDark.setAccent(currentColorsDark.getAccent());
        colorsDark.setDark(currentColorsDark.getDark());
        colorsDark.setLight(currentColorsDark.getLight());
        colorsDark.setPositive(currentColorsDark.getPositive());
        colorsDark.setNegative(currentColorsDark.getNegative());
        colorsDark.setInfo(currentColorsDark.getInfo());
        colorsDark.setWarning(currentColorsDark.getWarning());
        colorsRepository.save(colorsLight);
        colorsRepository.save(colorsDark);
    }

    @Override
    public void saveName(String name) {
        Colors currentColorsLight = colorsRepository.findByDefaultColorsIsFalseAndTheme(ColorTheme.LIGHT);
        Colors currentColorsDark = colorsRepository.findByDefaultColorsIsFalseAndTheme(ColorTheme.DARK);
        final Colors colorsLight = new Colors();
        colorsLight.setId(currentColorsLight.getId());
        colorsLight.setDefaultColors(false);
        colorsLight.setCorporateName(name);
        colorsLight.setTheme(currentColorsLight.getTheme());
        colorsLight.setLogoPath(currentColorsLight.getLogoPath());
        colorsLight.setBgC(currentColorsLight.getBgC());
        colorsLight.setTextC(currentColorsLight.getTextC());
        colorsLight.setPrimaryColor(currentColorsLight.getPrimaryColor());
        colorsLight.setSecondary(currentColorsLight.getSecondary());
        colorsLight.setAccent(currentColorsLight.getAccent());
        colorsLight.setDark(currentColorsLight.getDark());
        colorsLight.setLight(currentColorsLight.getLight());
        colorsLight.setPositive(currentColorsLight.getPositive());
        colorsLight.setNegative(currentColorsLight.getNegative());
        colorsLight.setInfo(currentColorsLight.getInfo());
        colorsLight.setWarning(currentColorsLight.getWarning());
        final Colors colorsDark = new Colors();
        colorsDark.setId(currentColorsDark.getId());
        colorsDark.setDefaultColors(false);
        colorsDark.setCorporateName(name);
        colorsDark.setTheme(currentColorsDark.getTheme());
        colorsDark.setLogoPath(currentColorsDark.getLogoPath());
        colorsDark.setBgC(currentColorsDark.getBgC());
        colorsDark.setTextC(currentColorsDark.getTextC());
        colorsDark.setPrimaryColor(currentColorsDark.getPrimaryColor());
        colorsDark.setSecondary(currentColorsDark.getSecondary());
        colorsDark.setAccent(currentColorsDark.getAccent());
        colorsDark.setDark(currentColorsDark.getDark());
        colorsDark.setLight(currentColorsDark.getLight());
        colorsDark.setPositive(currentColorsDark.getPositive());
        colorsDark.setNegative(currentColorsDark.getNegative());
        colorsDark.setInfo(currentColorsDark.getInfo());
        colorsDark.setWarning(currentColorsDark.getWarning());
        colorsRepository.save(colorsLight);
        colorsRepository.save(colorsDark);
    }

    /**
     * Speichert eine Liste von Farbkonfigurationen.
     *
     * @param defaultColors die Liste von Farbkonfigurationen, die gespeichert werden sollen
     */
    @Override
    public void saveAll(final List<Colors> defaultColors) {
        colorsRepository.saveAll(defaultColors);
    }


    /**
     * Löscht alle Farbkonfigurationen.
     */
    @Override
    public void deleteAll() {
        colorsRepository.deleteAll();
    }

}
