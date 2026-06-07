package com.battleship.ship;

import java.util.LinkedHashMap;
import java.util.Map;

public final class FleetConfiguration {
    private FleetConfiguration() {
    }

    public static Map<Integer, Integer> createDefaultFleet() {
        Map<Integer, Integer> shipsBySize = new LinkedHashMap<>();

        shipsBySize.put(6, 1);
        shipsBySize.put(5, 2);
        shipsBySize.put(4, 3);
        shipsBySize.put(3, 4);
        shipsBySize.put(2, 5);
        shipsBySize.put(1, 6);

        return shipsBySize;
    }
}
