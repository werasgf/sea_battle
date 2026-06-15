package com.battleship.bot;

import com.battleship.field.Coordinate;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class RandomSearchStrategy {

    private final Random random;

    public Coordinate chooseShot(KnowledgeBoard knowledgeBoard) {

        List<Coordinate> availableCoordinates = new ArrayList<>();

        for (int row = 0; row < knowledgeBoard.getSize(); row++) {
            for (int column = 0; column < knowledgeBoard.getSize(); column++) {

                Coordinate coordinate = new Coordinate(row, column);

                if (knowledgeBoard.canShootAt(coordinate)) {
                    availableCoordinates.add(coordinate);
                }
            }
        }

        return availableCoordinates.get(
                random.nextInt(availableCoordinates.size())
        );
    }
}