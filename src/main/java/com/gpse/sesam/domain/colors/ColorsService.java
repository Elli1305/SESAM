package com.gpse.sesam.domain.colors;

import java.util.List;

public interface ColorsService {

    Colors getColors(ColorTheme colorTheme);

    Colors changeColors(ColorTheme colorTheme, Colors colors);

    void resetColors(ColorTheme colorTheme);

    void saveAll(List<Colors> defaultColors);

    void deleteAll();

}
