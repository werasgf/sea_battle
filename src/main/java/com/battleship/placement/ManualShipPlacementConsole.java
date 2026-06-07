package com.battleship.placement;

import com.battleship.field.Board;
import com.battleship.field.BoardRenderer;
import com.battleship.io.ConsoleIO;
import com.battleship.player.Player;
import com.battleship.ship.FleetPlacementState;
import com.battleship.ship.ShipPlacementCommandParser;
import com.battleship.ship.ShipPlacementRequest;
import com.battleship.ship.ShipPlacementService;
import com.battleship.ship.exception.ShipPlacementException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ManualShipPlacementConsole {

    private final ConsoleIO consoleIO;
    private final BoardRenderer boardRenderer;
    private final ShipPlacementCommandParser commandParser;
    private final ShipPlacementService shipPlacementService;

    public void placeShips(Player player, Board board) {
        FleetPlacementState fleetPlacementState = FleetPlacementState.createDefault();

        printStartMessage(player);

        while (!fleetPlacementState.isCompleted()) {
            printCurrentState(board, fleetPlacementState);

            String input = consoleIO.readRequiredLine("Введите корабль: ");

            try {
                ShipPlacementRequest request = commandParser.parse(input);
                shipPlacementService.placeShip(board, fleetPlacementState, request);

                consoleIO.printLine("Корабль успешно установлен");
            } catch (ShipPlacementException exception) {
                consoleIO.printLine("Ошибка: " + exception.getMessage());
                consoleIO.printLine("Попробуйте ещё раз");
            }
        }

        consoleIO.printEmptyLine();
        consoleIO.printLine("Все корабли успешно расставлены");
        consoleIO.printLine("Итоговое поле:");
        boardRenderer.render(board);
    }

    private void printStartMessage(Player player) {
        consoleIO.printEmptyLine();
        consoleIO.printLine(player.name() + ", расставьте свои корабли");
        consoleIO.printLine("Формат команды: A1 H 6");
        consoleIO.printLine("A1 — начальная координата");
        consoleIO.printLine("H — горизонтально, V — вертикально");
        consoleIO.printLine("6 — размер корабля");
        consoleIO.printLine("Корабли нельзя ставить вплотную, включая диагонали");
    }

    private void printCurrentState(Board board, FleetPlacementState fleetPlacementState) {
        consoleIO.printEmptyLine();
        consoleIO.printLine("Текущее поле:");
        boardRenderer.render(board);

        consoleIO.printEmptyLine();
        consoleIO.printLine("Осталось расставить:");
        consoleIO.printLine(fleetPlacementState.remainingShipsAsText());
    }
}
