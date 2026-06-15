package com.battleship.bot;

import com.battleship.ship.FleetConfiguration;

import java.util.LinkedHashMap;
import java.util.Map;

public class RemainingFleet {

    private final Map<Integer, Integer> shipsBySize;

    public RemainingFleet() {
        this.shipsBySize = new LinkedHashMap<>(FleetConfiguration.createDefaultFleet());
    }

    public Map<Integer, Integer> asMap() {
        return Map.copyOf(shipsBySize);
    }

    public void markKilled(int shipSize) {
        int currentCount = shipsBySize.getOrDefault(shipSize, 0);

        if (currentCount <= 0) {
            throw new IllegalStateException("Кораблей размера " + shipSize + " уже не должно оставаться");
        }

        shipsBySize.put(shipSize, currentCount - 1);
    }
}