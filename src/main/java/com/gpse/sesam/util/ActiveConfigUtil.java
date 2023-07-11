package com.gpse.sesam.util;

import com.gpse.sesam.domain.location.door.TwoWayDoorConfig;

import java.time.LocalTime;
import java.util.List;

public final class ActiveConfigUtil {

    private ActiveConfigUtil() {

    }

    public static TwoWayDoorConfig getCurrentConfig(List<TwoWayDoorConfig> configs) {
        LocalTime currentTime = LocalTime.now();

        for (TwoWayDoorConfig config : configs) {
            if (!config.isBaseConfig()) {
                LocalTime startTime = LocalTime.parse(config.getStartTime());
                LocalTime endTime = LocalTime.parse(config.getEndTime());
                if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
                    return config;
                }
            }
        }

        for (TwoWayDoorConfig config : configs) {
            if (config.isBaseConfig()) {
                return config;
            }
        }
        return null;
    }
}
