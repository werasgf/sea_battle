package com.battleship.turn;

import com.battleship.bot.KnowledgeBoard;
import com.battleship.field.Board;
import com.battleship.field.BoardsPairRenderer;
import com.battleship.field.Coordinate;
import com.battleship.field.CoordinateParser;
import com.battleship.io.ConsoleIO;
import com.battleship.player.Player;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerTurnService {

    private final ConsoleIO consoleIO;
    private final CoordinateParser coordinateParser;
    private final BoardsPairRenderer boardsPairRenderer;

    public ShotResult makeTurn(
            Player player,
            Board playerBoard,
            Board enemyBoard,
            KnowledgeBoard playerKnowledgeBoard
    ) {
        consoleIO.printEmptyLine();
        consoleIO.printLine("Ход игрока: " + player.name());

        boardsPairRenderer.render(playerBoard, playerKnowledgeBoard);

        while (true) {
            String input = consoleIO.readRequiredLine("Введите координату выстрела: ");

            try {
                Coordinate coordinate = coordinateParser.parse(input);

                if (!playerKnowledgeBoard.canShootAt(coordinate)) {
                    consoleIO.printLine("По этой клетке уже стреляли или она недоступна. Выберите другую.");
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