package com.battleship.ship;

import com.battleship.field.Coordinate;
import com.battleship.field.Direction;

public record ShipPlacementRequest(
        Coordinate startCoordinate,
        Direction direction,
        int size
) {
}
