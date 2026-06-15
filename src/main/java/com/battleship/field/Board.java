package com.battleship.field;

import com.battleship.ship.Ship;
import com.battleship.turn.ShotResult;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
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
        return isInside(coordinate)
                && (cellStateAt(coordinate) == CellState.SHIP || cellStateAt(coordinate) == CellState.HIT);
    }

    public void addShip(Ship ship) {
        for (Coordinate coordinate : ship.getCoordinates()) {
            cells[coordinate.row()][coordinate.column()] = CellState.SHIP;
        }

        ships.add(ship);
    }

    public ShotResult shoot(Coordinate coordinate) {
        if (!isInside(coordinate)) {
            throw new IllegalArgumentException("Координата находится за пределами поля");
        }

        CellState currentState = cellStateAt(coordinate);

        if (currentState == CellState.MISS
                || currentState == CellState.HIT
                || currentState == CellState.FORBIDDEN) {
            return ShotResult.ALREADY_SHOT;
        }

        if (currentState == CellState.EMPTY) {
            cells[coordinate.row()][coordinate.column()] = CellState.MISS;
            return ShotResult.MISS;
        }

        Ship ship = findShipByCoordinate(coordinate)
                .orElseThrow(() -> new IllegalStateException("На поле есть клетка корабля, но сам корабль не найден"));

        ship.hit(coordinate);
        cells[coordinate.row()][coordinate.column()] = CellState.HIT;

        if (ship.isKilled()) {
            markCellsAroundAsForbidden(ship.getCoordinates());
            return ShotResult.KILLED;
        }

        return ShotResult.HIT;
    }

    public boolean allShipsKilled() {
        return !ships.isEmpty() && ships.stream().allMatch(Ship::isKilled);
    }

    public List<Ship> ships() {
        return Collections.unmodifiableList(ships);
    }

    public List<Coordinate> shipCoordinatesAt(Coordinate coordinate) {
        return findShipByCoordinate(coordinate)
                .map(Ship::getCoordinates)
                .orElseThrow(() -> new IllegalStateException("Корабль по координате не найден"));
    }

    private Optional<Ship> findShipByCoordinate(Coordinate coordinate) {
        return ships.stream()
                .filter(ship -> ship.contains(coordinate))
                .findFirst();
    }

    private void fillEmptyCells() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                cells[row][column] = CellState.EMPTY;
            }
        }
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
                            && cellStateAt(nearbyCoordinate) == CellState.EMPTY) {
                        cells[nearbyCoordinate.row()][nearbyCoordinate.column()] = CellState.FORBIDDEN;
                    }
                }
            }
        }
    }
}