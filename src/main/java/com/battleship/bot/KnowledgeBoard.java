package com.battleship.bot;

import com.battleship.field.Board;
import com.battleship.field.Coordinate;
import com.battleship.turn.ShotResult;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class KnowledgeBoard {

    private final int size;
    private final KnowledgeCellState[][] cells;

    public KnowledgeBoard() {
        this(Board.DEFAULT_SIZE);
    }

    public KnowledgeBoard(int size) {
        this.size = size;
        this.cells = new KnowledgeCellState[size][size];

        fillUnknownCells();
    }

    public KnowledgeCellState cellStateAt(Coordinate coordinate) {
        if (!isInside(coordinate)) {
            throw new IllegalArgumentException("Координата находится за пределами поля");
        }

        return cells[coordinate.row()][coordinate.column()];
    }

    public boolean isInside(Coordinate coordinate) {
        return coordinate.row() >= 0
                && coordinate.row() < size
                && coordinate.column() >= 0
                && coordinate.column() < size;
    }

    public boolean canShootAt(Coordinate coordinate) {
        return isInside(coordinate)
                && cellStateAt(coordinate) == KnowledgeCellState.UNKNOWN;
    }

    public List<Coordinate> activeHits() {
        List<Coordinate> result = new ArrayList<>();

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                Coordinate coordinate = new Coordinate(row, column);

                if (cellStateAt(coordinate) == KnowledgeCellState.HIT) {
                    result.add(coordinate);
                }
            }
        }

        return result;
    }

    public boolean hasActiveHits() {
        return !activeHits().isEmpty();
    }

    public void applyShotResult(Coordinate coordinate, ShotResult shotResult) {
        if (!isInside(coordinate)) {
            throw new IllegalArgumentException("Координата находится за пределами поля");
        }

        switch (shotResult) {
            case MISS -> cells[coordinate.row()][coordinate.column()] = KnowledgeCellState.MISS;
            case HIT -> cells[coordinate.row()][coordinate.column()] = KnowledgeCellState.HIT;
            case KILLED -> cells[coordinate.row()][coordinate.column()] = KnowledgeCellState.KILLED;
            case ALREADY_SHOT -> {
                // так нада.
            }
        }
    }

    public void markKilledShip(List<Coordinate> killedShipCoordinates) {
        for (Coordinate coordinate : killedShipCoordinates) {
            cells[coordinate.row()][coordinate.column()] = KnowledgeCellState.KILLED;
        }

        markCellsAroundAsForbidden(killedShipCoordinates);
    }

    private void markCellsAroundAsForbidden(List<Coordinate> shipCoordinates) {
        for (Coordinate shipCoordinate : shipCoordinates) {
            for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    Coordinate nearbyCoordinate = new Coordinate(
                            shipCoordinate.row() + rowOffset,
                            shipCoordinate.column() + columnOffset
                    );

                    if (isInside(nearbyCoordinate)
                            && cellStateAt(nearbyCoordinate) == KnowledgeCellState.UNKNOWN) {
                        cells[nearbyCoordinate.row()][nearbyCoordinate.column()] = KnowledgeCellState.FORBIDDEN;
                    }
                }
            }
        }
    }

    private void fillUnknownCells() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                cells[row][column] = KnowledgeCellState.UNKNOWN;
            }
        }
    }
}