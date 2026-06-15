package com.battleship.field;

public class CoordinateParser {

    private static final char FIRST_COLUMN_LETTER = 'A';
    private static final char LAST_COLUMN_LETTER = (char) (FIRST_COLUMN_LETTER + Board.DEFAULT_SIZE - 1);

    public Coordinate parse(String value) {
        if (value == null || value.length() < 2) {
            throw new IllegalArgumentException("Координата должна быть в формате A1-P16");
        }

        String normalizedValue = value.trim().toUpperCase();

        char columnLetter = normalizedValue.charAt(0);

        if (columnLetter < FIRST_COLUMN_LETTER || columnLetter > LAST_COLUMN_LETTER) {
            throw new IllegalArgumentException("Колонка должна быть от A до P");
        }

        String rowPart = normalizedValue.substring(1);
        int rowNumber;

        try {
            rowNumber = Integer.parseInt(rowPart);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Строка должна быть числом от 1 до 16");
        }

        if (rowNumber < 1 || rowNumber > Board.DEFAULT_SIZE) {
            throw new IllegalArgumentException("Строка должна быть от 1 до 16");
        }

        return new Coordinate(rowNumber - 1, columnLetter - FIRST_COLUMN_LETTER);
    }
}