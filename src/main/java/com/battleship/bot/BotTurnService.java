package com.battleship.bot;

import com.battleship.field.Board;
import com.battleship.field.Coordinate;
import com.battleship.io.ConsoleIO;
import com.battleship.turn.ShotResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BotTurnService {

    private final ConsoleIO consoleIO;

    private final RandomSearchStrategy randomSearchStrategy;

    private final AttackFinishingStrategy attackFinishingStrategy;

    private final BotTargetMemory targetMemory;

    public ShotResult makeTurn(
            Board playerBoard,
            KnowledgeBoard botKnowledgeBoard
    ) {

        Coordinate coordinate;

        if (targetMemory.hasActiveTarget()) {

            coordinate = attackFinishingStrategy.chooseShot(
                    botKnowledgeBoard,
                    targetMemory
            );

        } else {

            coordinate = randomSearchStrategy.chooseShot(
                    botKnowledgeBoard
            );
        }

        ShotResult result = playerBoard.shoot(coordinate);

        if (result == ShotResult.KILLED) {
            botKnowledgeBoard.markKilledShip(playerBoard.shipCoordinatesAt(coordinate));
        } else {
            botKnowledgeBoard.applyShotResult(coordinate, result);
        }

        updateMemory(
                coordinate,
                result
        );

        consoleIO.printLine(
                "Бот стреляет в "
                        + coordinate.toConsoleValue()
        );

        consoleIO.printLine(
                "Результат: "
                        + result.getTitle()
        );

        return result;
    }

    private void updateMemory(Coordinate coordinate, ShotResult result) {
        if (result == ShotResult.HIT) {
            targetMemory.addHit(coordinate);
        }

        if (result == ShotResult.KILLED) {
            targetMemory.clear();
        }
    }
}