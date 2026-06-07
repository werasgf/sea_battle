package com.battleship.ship;

import com.battleship.field.Board;
import com.battleship.field.Coordinate;
import com.battleship.field.Direction;
import com.battleship.ship.exception.ShipPlacementException;

public class ShipPlacementCommandParser {
    private static final char FIRST_COLUMN_LETTER = 'A';
    private static final char LAST_COLUMN_LETTER = (char) (FIRST_COLUMN_LETTER + Board.DEFAULT_SIZE - 1);

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
        if (value == null || value.length() < 2) {
            throw new ShipPlacementException("Координата должна быть в формате A1-P16");
        }

        String normalizedValue = value.trim().toUpperCase();

        char columnLetter = normalizedValue.charAt(0);

        if (columnLetter < FIRST_COLUMN_LETTER || columnLetter > LAST_COLUMN_LETTER) {
            throw new ShipPlacementException("Колонка должна быть от A до P");
        }

        String rowPart = normalizedValue.substring(1);
        int rowNumber;

        try {
            rowNumber = Integer.parseInt(rowPart);
        } catch (NumberFormatException exception) {
            throw new ShipPlacementException("Строка должна быть числом от 1 до 16");
        }

        if (rowNumber < 1 || rowNumber > Board.DEFAULT_SIZE) {
            throw new ShipPlacementException("Строка должна быть от 1 до 16");
        }

        int row = rowNumber - 1;
        int column = columnLetter - FIRST_COLUMN_LETTER;

        return new Coordinate(row, column);
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
