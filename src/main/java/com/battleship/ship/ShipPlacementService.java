package com.battleship.ship;

import com.battleship.field.Board;
import com.battleship.field.Coordinate;
import com.battleship.field.Direction;
import com.battleship.ship.exception.ShipPlacementException;

import java.util.ArrayList;
import java.util.List;

public class ShipPlacementService {

    public void placeShip(
            Board board,
            FleetPlacementState fleetPlacementState,
            ShipPlacementRequest request
    ) {
        validateShipSize(fleetPlacementState, request.size());

        List<Coordinate> shipCoordinates = buildShipCoordinates(request);

        validateInsideBoard(board, request, shipCoordinates);
        validateDistanceFromOtherShips(board, shipCoordinates);

        Ship ship = new Ship(request.size(), shipCoordinates);

        board.addShip(ship);
        fleetPlacementState.markPlaced(request.size());
    }

    private void validateShipSize(FleetPlacementState fleetPlacementState, int size) {
        if (size <= 0) {
            throw new ShipPlacementException("Размер корабля должен быть положительным");
        }

        if (!fleetPlacementState.containsSize(size)) {
            throw new ShipPlacementException("Кораблей размера " + size + " нет в правилах игры");
        }

        if (fleetPlacementState.hasRemainingShipOfSize(size)) {
            throw new ShipPlacementException("Все корабли размера " + size + " уже расставлены");
        }
    }

    private List<Coordinate> buildShipCoordinates(ShipPlacementRequest request) {
        List<Coordinate> coordinates = new ArrayList<>();

        for (int offset = 0; offset < request.size(); offset++) {
            int row = request.startCoordinate().row();
            int column = request.startCoordinate().column();

            if (request.direction() == Direction.HORIZONTAL) {
                column += offset;
            }

            if (request.direction() == Direction.VERTICAL) {
                row += offset;
            }

            coordinates.add(new Coordinate(row, column));
        }

        return coordinates;
    }

    private void validateInsideBoard(Board board, ShipPlacementRequest request, List<Coordinate> coordinates) {
        for (Coordinate coordinate : coordinates) {
            if (!board.isInside(coordinate)) {
                throw new ShipPlacementException(
                        "Корабль размера " + request.size() +
                                " не помещается на поле от координаты " +
                                request.startCoordinate().toConsoleValue() +
                                " в выбранном направлении. " +
                                "Поле ограничено колонками A-P и строками 1-16."
                );
            }
        }
    }

    private void validateDistanceFromOtherShips(Board board, List<Coordinate> coordinates) {
        for (Coordinate coordinate : coordinates) {
            if (hasShipAround(board, coordinate)) {
                throw new ShipPlacementException(
                        "Корабль пересекается или стоит слишком близко к другому кораблю. " +
                                "Между кораблями должна быть минимум одна клетка"
                );
            }
        }
    }

    private boolean hasShipAround(Board board, Coordinate coordinate) {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                Coordinate nearbyCoordinate = new Coordinate(
                        coordinate.row() + rowOffset,
                        coordinate.column() + columnOffset
                );

                if (board.isInside(nearbyCoordinate) && board.hasShipAt(nearbyCoordinate)) {
                    return true;
                }
            }
        }

        return false;
    }
}
