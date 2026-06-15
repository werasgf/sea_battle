package com.battleship.turn;

import com.battleship.bot.KnowledgeBoard;
import com.battleship.bot.KnowledgeBoardRenderer;
import com.battleship.field.Board;
import com.battleship.field.Coordinate;
import com.battleship.field.CoordinateParser;
import com.battleship.io.ConsoleIO;
import com.battleship.player.Player;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerTurnService {

    private final ConsoleIO consoleIO;
    private final CoordinateParser coordinateParser;
    private final KnowledgeBoardRenderer knowledgeBoardRenderer;

    public ShotResult makeTurn(
            Player player,
            Board enemyBoard,
            KnowledgeBoard playerKnowledgeBoard
    ) {
        consoleIO.printEmptyLine();
        consoleIO.printLine("Ход игрока: " + player.name());
        consoleIO.printLine("Поле соперника:");
        knowledgeBoardRenderer.render(playerKnowledgeBoard);

        while (true) {
            String input = consoleIO.readRequiredLine("Введите координату выстрела: ");

            try {
                Coordinate coordinate = coordinateParser.parse(input);

                if (!playerKnowledgeBoard.canShootAt(coordinate)) {
                    consoleIO.printLine("По этой клетке уже стреляли или она недоступна. Выберите другую");
                    continue;
                }

                ShotResult shotResult = enemyBoard.shoot(coordinate);

                if (shotResult == ShotResult.KILLED) {
                    playerKnowledgeBoard.markKilledShip(enemyBoard.shipCoordinatesAt(coordinate));
                } else {
                    playerKnowledgeBoard.applyShotResult(coordinate, shotResult);
                }

                consoleIO.printLine("Результат выстрела: " + shotResult.getTitle());

                return shotResult;
            } catch (IllegalArgumentException exception) {
                consoleIO.printLine("Ошибка: " + exception.getMessage());
            }
        }
    }
}