package com.battleship.ship;

import com.battleship.ship.exception.ShipPlacementException;

import java.util.LinkedHashMap;
import java.util.Map;

public class FleetPlacementState {
    private final Map<Integer, Integer> remainingShipsBySize;

    public FleetPlacementState(Map<Integer, Integer> remainingShipsBySize) {
        this.remainingShipsBySize = new LinkedHashMap<>(remainingShipsBySize);
    }

    public static FleetPlacementState createDefault() {
        return new FleetPlacementState(FleetConfiguration.createDefaultFleet());
    }

    public boolean isCompleted() {
        return remainingShipsBySize.values()
                .stream()
                .allMatch(count -> count == 0);
    }

    public boolean containsSize(int size) {
        return remainingShipsBySize.containsKey(size);
    }

    public boolean hasRemainingShipOfSize(int size) {
        return remainingShipsBySize.getOrDefault(size, 0) <= 0;
    }

    public void markPlaced(int size) {
        if (hasRemainingShipOfSize(size)) {
            throw new ShipPlacementException("Корабли размера " + size + " уже расставлены");
        }

        remainingShipsBySize.compute(size, (k, currentCount) -> currentCount - 1);
    }

    public String remainingShipsAsText() {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<Integer, Integer> entry : remainingShipsBySize.entrySet()) {
            int shipSize = entry.getKey();
            int count = entry.getValue();

            if (count > 0) {
                builder.append("- размер ")
                        .append(shipSize)
                        .append(": ")
                        .append(count)
                        .append(System.lineSeparator());
            }
        }

        return builder.toString();
    }
}
