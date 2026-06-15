package com.battleship.field;

import com.battleship.bot.KnowledgeBoard;
import com.battleship.bot.KnowledgeCellState;
import com.battleship.io.ConsoleIO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardsPairRenderer {

    private static final char FIRST_COLUMN_LETTER = 'A';
    private static final String GAP = "      ";

    private final ConsoleIO consoleIO;

    public void render(Board playerBoard, KnowledgeBoard enemyKnowledgeBoard) {
        consoleIO.printLine("Ваше поле" + GAP.repeat(6) + "Поле соперника");
        consoleIO.printLine(header(playerBoard.getSize()) + GAP + header(enemyKnowledgeBoard.getSize()));

        for (int row = 0; row < playerBoard.getSize(); row++) {
            String left = boardRow(playerBoard, row);
            String right = knowledgeBoardRow(enemyKnowledgeBoard, row);

            consoleIO.printLine(left + GAP + right);
        }
    }

    private String header(int size) {
        StringBuilder builder = new StringBuilder("    ");

        for (int column = 0; column < size; column++) {
            builder.append((char) (FIRST_COLUMN_LETTER + column)).append(' ');
        }

        return builder.toString();
    }

    private String boardRow(Board board, int row) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("%2d  ", row + 1));

        for (int column = 0; column < board.getSize(); column++) {
            Coordinate coordinate = new Coordinate(row, column);
            builder.append(toBoardSymbol(board.cellStateAt(coordinate))).append(' ');
        }

        return builder.toString();
    }

    private String knowledgeBoardRow(KnowledgeBoard board, int row) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("%2d  ", row + 1));

        for (int column = 0; column < board.getSize(); column++) {
            Coordinate coordinate = new Coordinate(row, column);
            builder.append(toKnowledgeSymbol(board.cellStateAt(coordinate))).append(' ');
        }

        return builder.toString();
    }

    private String toBoardSymbol(CellState cellState) {
        return switch (cellState) {
            case EMPTY -> ".";
            case SHIP -> "■";
            case MISS, FORBIDDEN -> "☉";
            case HIT -> "⛝";
        };
    }

    private String toKnowledgeSymbol(KnowledgeCellState cellState) {
        return switch (cellState) {
            case UNKNOWN -> ".";
            case MISS, FORBIDDEN -> "☉";
            case HIT, KILLED -> "⛝";
        };
    }
}
