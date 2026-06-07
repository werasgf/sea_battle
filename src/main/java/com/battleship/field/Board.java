package com.battleship.field;

import com.battleship.ship.Ship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    public static final int DEFAULT_SIZE = 16;

    private final int size;
    private final CellState[][] cells;
    private final List<Ship> ships = new ArrayList<>();

    public Board() {
        this(DEFAULT_SIZE);
    }

    public Board(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Размер поля должен быть положительным");
        }

        this.size = size;
        this.cells = new CellState[size][size];

        fillEmptyCells();
    }

    public int size() {
        return size;
    }

    public CellState cellStateAt(Coordinate coordinate) {
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

    public boolean hasShipAt(Coordinate coordinate) {
        return isInside(coordinate) && cellStateAt(coordinate) == CellState.SHIP;
    }

    public void addShip(Ship ship) {
        for (Coordinate coordinate : ship.coordinates()) {
            cells[coordinate.row()][coordinate.column()] = CellState.SHIP;
        }

        ships.add(ship);
    }

    public List<Ship> ships() {
        return Collections.unmodifiableList(ships);
    }

    private void fillEmptyCells() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                cells[row][column] = CellState.EMPTY;
            }
        }
    }
}
