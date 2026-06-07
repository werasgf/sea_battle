package com.battleship.field;

public record Coordinate(int row, int column) {
    public String toConsoleValue() {
        char columnLetter = (char) ('A' + column);
        int rowNumber = row + 1;

        return columnLetter + String.valueOf(rowNumber);
    }
}
