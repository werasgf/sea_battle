package com.battleship.game;

import com.battleship.field.Board;
import com.battleship.field.BoardRenderer;
import com.battleship.io.ConsoleIO;
import com.battleship.placement.AutoShipPlacementService;
import com.battleship.placement.ManualShipPlacementConsole;
import com.battleship.placement.ShipPlacementMode;
import com.battleship.placement.ShipPlacementModeSelector;
import com.battleship.player.Player;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SinglePlayerGame implements Game {

    private static final String BOT_NAME = "Ботярик";

    private final ConsoleIO consoleIO;
    private final ManualShipPlacementConsole manualShipPlacementConsole;
    private final AutoShipPlacementService autoShipPlacementService;
    private final BoardRenderer boardRenderer;
    private final ShipPlacementModeSelector shipPlacementModeSelector;

    @Override
    public void start(Player player) {
        Player bot = new Player(BOT_NAME);

        Board playerBoard = new Board();
        Board botBoard = new Board();

        consoleIO.printEmptyLine();
        consoleIO.printLine(player.name() + ", выбран режим: одиночная игра");
        consoleIO.printLine("Ваш соперник: " + bot.name());
        consoleIO.printLine("Размер поля игрока: " + playerBoard.size() + "x" + playerBoard.size());
        consoleIO.printLine("Размер поля соперника: " + botBoard.size() + "x" + botBoard.size());
        consoleIO.printLine("Подготовка игры началась");

        placePlayerShips(player, playerBoard);

        consoleIO.printEmptyLine();
        consoleIO.printLine("Расстановка кораблей завершена.");
        consoleIO.printLine("Дальше будет реализован игровой цикл ходов.");
    }

    private void placePlayerShips(Player player, Board playerBoard) {
        consoleIO.printEmptyLine();

        ShipPlacementMode placementMode = shipPlacementModeSelector.select();

        if (placementMode == ShipPlacementMode.MANUAL) {
            manualShipPlacementConsole.placeShips(player, playerBoard);
            return;
        }

        autoShipPlacementService.placeShips(playerBoard);

        consoleIO.printEmptyLine();
        consoleIO.printLine("Корабли игрока расставлены автоматически");
        consoleIO.printLine("Ваше поле:");
        boardRenderer.render(playerBoard);
    }
}