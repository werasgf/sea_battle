package com.battleship.ship;

import com.battleship.field.Coordinate;
import com.battleship.field.CoordinateParser;
import com.battleship.field.Direction;
import com.battleship.ship.exception.ShipPlacementException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShipPlacementCommandParser {

    private final CoordinateParser coordinateParser;

    public ShipPlacementRequest parse(String input) {
        String[] parts = input.trim().split("\\s+");

        if (parts.length != 3) {
            throw new ShipPlacementException("Введите команду в формате: A1 H 6");
        }

        Coordinate startCoordinate = parseCoordinate(parts[0]);
        Direction direction = parseDirection(parts[1]);
        int size = parseSize(parts[2]);

        return new ShipPlacementRequest(startCoordinate, direction, size);
    }

    private Coordinate parseCoordinate(String value) {
        try {
            return coordinateParser.parse(value);
        } catch (IllegalArgumentException exception) {
            throw new ShipPlacementException(exception.getMessage());
        }
    }

    private Direction parseDirection(String value) {
        return Direction.fromConsoleValue(value)
                .orElseThrow(() -> new ShipPlacementException("Направление должно быть H или V"));
    }

    private int parseSize(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new ShipPlacementException("Размер корабля должен быть числом");
        }
    }
}
