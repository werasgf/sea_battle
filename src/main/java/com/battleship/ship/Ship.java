package com.battleship.ship;

import com.battleship.field.Coordinate;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class Ship {

    private final int size;
    private final List<Coordinate> coordinates;
    private final Set<Coordinate> hitCoordinates = new HashSet<>();

    public Ship(int size, List<Coordinate> coordinates) {
        this.coordinates = List.copyOf(coordinates);

        if (size <= 0) {
            throw new IllegalArgumentException("Размер корабля должен быть положительным");
        }

        if (size != coordinates.size()) {
            throw new IllegalArgumentException("Размер корабля не совпадает с количеством координат");
        }

        this.size = size;
    }

    public boolean contains(Coordinate coordinate) {
        return coordinates.contains(coordinate);
    }

    public void hit(Coordinate coordinate) {
        if (!contains(coordinate)) {
            throw new IllegalArgumentException("Координата не принадлежит кораблю");
        }

        hitCoordinates.add(coordinate);
    }

    public boolean isKilled() {
        return hitCoordinates.size() == size;
    }
}