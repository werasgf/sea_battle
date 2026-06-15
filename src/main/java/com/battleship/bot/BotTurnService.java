package com.battleship.bot;

import com.battleship.field.Board;
import com.battleship.field.Coordinate;
import com.battleship.io.ConsoleIO;
import com.battleship.turn.ShotResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BotTurnService {

    private final ConsoleIO consoleIO;
    private final ProbabilitySearchStrategy probabilitySearchStrategy;
    private final AttackFinishingStrategy attackFinishingStrategy;
    private final BotTargetMemory targetMemory;
    private final RemainingFleet remainingFleet;

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
            coordinate = probabilitySearchStrategy.chooseShot(
                    botKnowledgeBoard,
                    remainingFleet.asMap()
            );
        }

        ShotResult result = playerBoard.shoot(coordinate);

        if (result == ShotResult.KILLED) {
            botKnowledgeBoard.markKilledShip(playerBoard.shipCoordinatesAt(coordinate));
        } else {
            botKnowledgeBoard.applyShotResult(coordinate, result);
        }

        updateMemoryAndFleet(coordinate, result, playerBoard);

        consoleIO.printLine("Бот стреляет в " + coordinate.toConsoleValue());
        consoleIO.printLine("Результат: " + result.getTitle());

        return result;
    }

    private void updateMemoryAndFleet(
            Coordinate coordinate,
            ShotResult result,
            Board playerBoard
    ) {
        if (result == ShotResult.HIT) {
            targetMemory.addHit(coordinate);
            return;
        }

        if (result == ShotResult.KILLED) {
            int killedShipSize = playerBoard.shipSizeAt(coordinate);

            remainingFleet.markKilled(killedShipSize);
            targetMemory.clear();
        }
    }
}