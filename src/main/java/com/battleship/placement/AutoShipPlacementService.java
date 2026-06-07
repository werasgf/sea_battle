package com.battleship.placement;

import com.battleship.field.Board;
import com.battleship.field.Coordinate;
import com.battleship.field.Direction;
import com.battleship.ship.*;
import com.battleship.ship.exception.ShipPlacementException;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class AutoShipPlacementService {

    private static final int MAX_BOARD_GENERATION_ATTEMPTS = 100;

    private final ShipPlacementService shipPlacementService;
    private final Random random;

    public AutoShipPlacementService(ShipPlacementService shipPlacementService) {
        this(shipPlacementService, new Random());
    }

    public void placeShips(Board board) {
        if (!board.ships().isEmpty()) {
            throw new AutoShipPlacementException("Автоматическая расстановка доступна только для пустого поля");
        }

        Board generatedBoard = generateBoard();

        for (Ship ship : generatedBoard.ships()) {
            board.addShip(ship);
        }
    }

    private Board generateBoard() {
        for (int attempt = 1; attempt <= MAX_BOARD_GENERATION_ATTEMPTS; attempt++) {
            Board board = new Board();
            FleetPlacementState fleetPlacementState = FleetPlacementState.createDefault();

            boolean placementCompleted = tryPlaceFleet(board, fleetPlacementState);

            if (placementCompleted) {
                return board;
            }
        }

        throw new AutoShipPlacementException(
                "Не удалось автоматически расставить корабли. Попробуйте запустить расстановку ещё раз"
        );
    }

    private boolean tryPlaceFleet(Board board, FleetPlacementState fleetPlacementState) {
        for (Map.Entry<Integer, Integer> entry : FleetConfiguration.createDefaultFleet().entrySet()) {
            int shipSize = entry.getKey();
            int shipsCount = entry.getValue();

            for (int currentShip = 0; currentShip < shipsCount; currentShip++) {
                boolean placed = tryPlaceShip(board, fleetPlacementState, shipSize);

                if (!placed) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean tryPlaceShip(
            Board board,
            FleetPlacementState fleetPlacementState,
            int shipSize
    ) {
        List<ShipPlacementRequest> candidates = createPlacementCandidates(shipSize);

        Collections.shuffle(candidates, random);

        for (ShipPlacementRequest candidate : candidates) {
            if (tryPlaceCandidate(board, fleetPlacementState, candidate)) {
                return true;
            }
        }

        return false;
    }

    private List<ShipPlacementRequest> createPlacementCandidates(int shipSize) {
        List<ShipPlacementRequest> candidates = new ArrayList<>();

        for (int row = 0; row < Board.DEFAULT_SIZE; row++) {
            for (int column = 0; column < Board.DEFAULT_SIZE; column++) {
                Coordinate startCoordinate = new Coordinate(row, column);

                candidates.add(new ShipPlacementRequest(
                        startCoordinate,
                        Direction.HORIZONTAL,
                        shipSize
                ));

                candidates.add(new ShipPlacementRequest(
                        startCoordinate,
                        Direction.VERTICAL,
                        shipSize
                ));
            }
        }

        return candidates;
    }

    private boolean tryPlaceCandidate(
            Board board,
            FleetPlacementState fleetPlacementState,
            ShipPlacementRequest candidate
    ) {
        try {
            shipPlacementService.placeShip(board, fleetPlacementState, candidate);
            return true;
        } catch (ShipPlacementException exception) {
            return false;
        }
    }
}
