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

    public ColorsServiceImpl(ColorsRepository colorsRepository) {
        this.colorsRepository = colorsRepository;
    }

    /**
     * Ruft die Farbkonfiguration für das angegebene Farbthema ab.
     *
     * @param colorTheme das Farbthema, für das die Farben abgerufen werden sollen
     * @return die Farbkonfiguration für das angegebene Farbthema
     */
    public Colors getColors(ColorTheme colorTheme) {
        return colorsRepository.findByDefaultColorsIsFalseAndTheme(colorTheme);
    }


    /**
     * Ändert die Farbkonfiguration für das angegebene Farbthema.
     *
     * @param colorTheme das Farbthema, für das die Farben geändert werden sollen
     * @param colors     die neue Farbkonfiguration
     * @return die aktualisierte Farbkonfiguration
     */
    @Override
    public Colors changeColors(ColorTheme colorTheme, Colors colors) {
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
    public void resetColors(ColorTheme colorTheme) {
        Long currentColorsId = colorsRepository.findByDefaultColorsIsFalseAndTheme(colorTheme).getId();
        Colors defaultColors = colorsRepository.findByDefaultColorsIsTrueAndTheme(colorTheme);
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

    /**
     * Speichert eine Liste von Farbkonfigurationen.
     *
     * @param defaultColors die Liste von Farbkonfigurationen, die gespeichert werden sollen
     */
    @Override
    public void saveAll(List<Colors> defaultColors) {
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
