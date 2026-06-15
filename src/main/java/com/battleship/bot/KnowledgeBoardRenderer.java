package com.battleship.bot;

import com.battleship.field.Coordinate;
import com.battleship.io.ConsoleIO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KnowledgeBoardRenderer {

    private static final char FIRST_COLUMN_LETTER = 'A';

    private final ConsoleIO consoleIO;

    public void render(KnowledgeBoard knowledgeBoard) {
        printHeader(knowledgeBoard);

        for (int row = 0; row < knowledgeBoard.getSize(); row++) {
            StringBuilder line = new StringBuilder();

            line.append(String.format("%2d  ", row + 1));

            for (int column = 0; column < knowledgeBoard.getSize(); column++) {
                Coordinate coordinate = new Coordinate(row, column);
                KnowledgeCellState cellState = knowledgeBoard.cellStateAt(coordinate);

                line.append(toSymbol(cellState)).append(' ');
            }

            consoleIO.printLine(line.toString());
        }
    }

    private void printHeader(KnowledgeBoard knowledgeBoard) {
        StringBuilder header = new StringBuilder("    ");

        for (int column = 0; column < knowledgeBoard.getSize(); column++) {
            char columnLetter = (char) (FIRST_COLUMN_LETTER + column);
            header.append(columnLetter).append(' ');
        }

        consoleIO.printLine(header.toString());
    }

    private String toSymbol(KnowledgeCellState cellState) {
        return switch (cellState) {
            case UNKNOWN -> ".";
            case MISS, FORBIDDEN -> "☉";
            case HIT, KILLED -> "⛝";
        };
    }
}