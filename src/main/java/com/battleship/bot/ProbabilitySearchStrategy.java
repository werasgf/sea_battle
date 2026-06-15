package com.battleship.bot;

import com.battleship.field.Coordinate;
import com.battleship.field.Direction;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
public class ProbabilitySearchStrategy {

    private final Random random;

    public Coordinate chooseShot(
            KnowledgeBoard knowledgeBoard,
            Map<Integer, Integer> remainingShipsBySize
    ) {
        int[][] weights = buildProbabilityMap(
                knowledgeBoard,
                remainingShipsBySize
        );

        return chooseBestCoordinate(knowledgeBoard, weights);
    }

    private int[][] buildProbabilityMap(
            KnowledgeBoard knowledgeBoard,
            Map<Integer, Integer> remainingShipsBySize
    ) {
        int[][] weights = new int[knowledgeBoard.getSize()][knowledgeBoard.getSize()];

        for (Map.Entry<Integer, Integer> entry : remainingShipsBySize.entrySet()) {
            int shipSize = entry.getKey();
            int count = entry.getValue();

            if (count <= 0) {
                continue;
            }

            addWeightsForShipSize(
                    knowledgeBoard,
                    weights,
                    shipSize,
                    count
            );
        }

        return weights;
    }

    private void addWeightsForShipSize(
            KnowledgeBoard knowledgeBoard,
            int[][] weights,
            int shipSize,
            int shipsCount
    ) {
        for (int row = 0; row < knowledgeBoard.getSize(); row++) {
            for (int column = 0; column < knowledgeBoard.getSize(); column++) {
                Coordinate startCoordinate = new Coordinate(row, column);

                addWeightsIfPlacementPossible(
                        knowledgeBoard,
                        weights,
                        startCoordinate,
                        Direction.HORIZONTAL,
                        shipSize,
                        shipsCount
                );

                addWeightsIfPlacementPossible(
                        knowledgeBoard,
                        weights,
                        startCoordinate,
                        Direction.VERTICAL,
                        shipSize,
                        shipsCount
                );
            }
        }
    }

    private void addWeightsIfPlacementPossible(
            KnowledgeBoard knowledgeBoard,
            int[][] weights,
            Coordinate startCoordinate,
            Direction direction,
            int shipSize,
            int shipsCount
    ) {
        List<Coordinate> coordinates = buildCoordinates(
                startCoordinate,
                direction,
                shipSize
        );

        if (!isPlacementPossible(knowledgeBoard, coordinates)) {
            return;
        }

        for (Coordinate coordinate : coordinates) {
            if (knowledgeBoard.canShootAt(coordinate)) {
                weights[coordinate.row()][coordinate.column()] += shipsCount;
            }
        }
    }

    private List<Coordinate> buildCoordinates(
            Coordinate startCoordinate,
            Direction direction,
            int shipSize
    ) {
        List<Coordinate> coordinates = new ArrayList<>();

        for (int offset = 0; offset < shipSize; offset++) {
            int row = startCoordinate.row();
            int column = startCoordinate.column();

            if (direction == Direction.HORIZONTAL) {
                column += offset;
            } else {
                row += offset;
            }

            coordinates.add(new Coordinate(row, column));
        }

        return coordinates;
    }

    private boolean isPlacementPossible(
            KnowledgeBoard knowledgeBoard,
            List<Coordinate> coordinates
    ) {
        for (Coordinate coordinate : coordinates) {
            if (!knowledgeBoard.isInside(coordinate)) {
                return false;
            }

            KnowledgeCellState cellState = knowledgeBoard.cellStateAt(coordinate);

            if (cellState == KnowledgeCellState.MISS
                    || cellState == KnowledgeCellState.KILLED
                    || cellState == KnowledgeCellState.FORBIDDEN) {
                return false;
            }
        }

        return true;
    }

    private Coordinate chooseBestCoordinate(
            KnowledgeBoard knowledgeBoard,
            int[][] weights
    ) {
        int maxWeight = 0;
        List<Coordinate> bestCoordinates = new ArrayList<>();

        for (int row = 0; row < knowledgeBoard.getSize(); row++) {
            for (int column = 0; column < knowledgeBoard.getSize(); column++) {
                Coordinate coordinate = new Coordinate(row, column);

                if (!knowledgeBoard.canShootAt(coordinate)) {
                    continue;
                }

                int weight = weights[row][column];

                if (weight > maxWeight) {
                    maxWeight = weight;
                    bestCoordinates.clear();
                    bestCoordinates.add(coordinate);
                } else if (weight == maxWeight) {
                    bestCoordinates.add(coordinate);
                }
            }
        }

        if (bestCoordinates.isEmpty()) {
            throw new IllegalStateException("Бот не нашел доступную клетку для выстрела");
        }

        return bestCoordinates.get(random.nextInt(bestCoordinates.size()));
    }
}