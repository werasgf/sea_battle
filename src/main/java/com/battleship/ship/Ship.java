package com.battleship.ship;

import com.battleship.field.Coordinate;

import java.util.List;

public record Ship(int size, List<Coordinate> coordinates) {
    public Ship {
        coordinates = List.copyOf(coordinates);

        if (size <= 0) {
            throw new IllegalArgumentException("Размер корабля должен быть положительным");
        }

        if (size != coordinates.size()) {
            throw new IllegalArgumentException("Размер корабля не совпадает с количеством координат");
        }
    }
}
