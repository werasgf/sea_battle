package com.battleship.bot;

import com.battleship.field.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class BotTargetMemory {

    private final List<Coordinate> hitCoordinates = new ArrayList<>();

    public void addHit(Coordinate coordinate) {
        hitCoordinates.add(coordinate);
    }

    public void clear() {
        hitCoordinates.clear();
    }

    public boolean hasActiveTarget() {
        return !hitCoordinates.isEmpty();
    }

    public List<Coordinate> getHits() {
        return List.copyOf(hitCoordinates);
    }
}