package com.battleship.bot;

import com.battleship.field.Coordinate;

import java.util.Comparator;
import java.util.List;

public class AttackFinishingStrategy {

    public Coordinate chooseShot(
            KnowledgeBoard knowledgeBoard,
            BotTargetMemory memory
    ) {
        List<Coordinate> hits = memory.getHits();

        if (hits.isEmpty()) {
            throw new IllegalStateException("У бота нет активной цели");
        }

        if (hits.size() == 1) {
            return chooseAroundSingleHit(knowledgeBoard, hits.getFirst());
        }

        if (isHorizontal(hits)) {
            return chooseHorizontalContinuation(knowledgeBoard, hits);
        }

        if (isVertical(hits)) {
            return chooseVerticalContinuation(knowledgeBoard, hits);
        }

        throw new IllegalStateException("Попадания бота не образуют корректный корабль");
    }

    private Coordinate chooseAroundSingleHit(
            KnowledgeBoard knowledgeBoard,
            Coordinate hit
    ) {
        List<Coordinate> candidates = List.of(
                new Coordinate(hit.row() - 1, hit.column()),
                new Coordinate(hit.row() + 1, hit.column()),
                new Coordinate(hit.row(), hit.column() - 1),
                new Coordinate(hit.row(), hit.column() + 1)
        );

        return candidates.stream()
                .filter(knowledgeBoard::canShootAt)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Не удалось найти клетку для добивания"));
    }

    private Coordinate chooseHorizontalContinuation(
            KnowledgeBoard knowledgeBoard,
            List<Coordinate> hits
    ) {
        List<Coordinate> sortedHits = hits.stream()
                .sorted(Comparator.comparingInt(Coordinate::column))
                .toList();

        Coordinate left = sortedHits.getFirst();
        Coordinate right = sortedHits.getLast();

        List<Coordinate> candidates = List.of(
                new Coordinate(left.row(), left.column() - 1),
                new Coordinate(right.row(), right.column() + 1)
        );

        return candidates.stream()
                .filter(knowledgeBoard::canShootAt)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Не удалось продолжить горизонтальную атаку"));
    }

    private Coordinate chooseVerticalContinuation(
            KnowledgeBoard knowledgeBoard,
            List<Coordinate> hits
    ) {
        List<Coordinate> sortedHits = hits.stream()
                .sorted(Comparator.comparingInt(Coordinate::row))
                .toList();

        Coordinate top = sortedHits.getFirst();
        Coordinate bottom = sortedHits.getLast();

        List<Coordinate> candidates = List.of(
                new Coordinate(top.row() - 1, top.column()),
                new Coordinate(bottom.row() + 1, bottom.column())
        );

        return candidates.stream()
                .filter(knowledgeBoard::canShootAt)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Не удалось продолжить вертикальную атаку"));
    }

    private boolean isHorizontal(List<Coordinate> hits) {
        int row = hits.getFirst().row();

        return hits.stream()
                .allMatch(coordinate -> coordinate.row() == row);
    }

    private boolean isVertical(List<Coordinate> hits) {
        int column = hits.getFirst().column();

        return hits.stream()
                .allMatch(coordinate -> coordinate.column() == column);
    }
}