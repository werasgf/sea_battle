package com.battleship.field;

import com.battleship.io.ConsoleIO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardRenderer {
    private static final char FIRST_COLUMN_LETTER = 'A';

    private final ConsoleIO consoleIO;

    public void render(Board board) {
        printHeader(board);

        for (int row = 0; row < board.size(); row++) {
            StringBuilder line = new StringBuilder();

            line.append(String.format("%2d  ", row + 1));

            for (int column = 0; column < board.size(); column++) {
                Coordinate coordinate = new Coordinate(row, column);
                CellState cellState = board.cellStateAt(coordinate);

                line.append(toSymbol(cellState)).append(' ');
            }

            consoleIO.printLine(line.toString());
        }
    }

    private void printHeader(Board board) {
        StringBuilder header = new StringBuilder("    ");

        for (int column = 0; column < board.size(); column++) {
            char columnLetter = (char) (FIRST_COLUMN_LETTER + column);
            header.append(columnLetter).append(' ');
        }

        consoleIO.printLine(header.toString());
    }

    private String toSymbol(CellState cellState) {
        return switch (cellState) {
            case EMPTY -> ".";
            case SHIP -> "■";
        };
    }
}
