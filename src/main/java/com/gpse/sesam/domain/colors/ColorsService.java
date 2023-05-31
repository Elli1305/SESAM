package com.gpse.sesam.domain.colors;

import java.util.List;

public interface ColorsService {

    Colors getColors();

    Colors changeColors(Colors colors);

    void resetColors();

    void saveAll(List<Colors> defaultColors);

    void deleteAll();

}
