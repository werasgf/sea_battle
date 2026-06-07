package com.battleship.game;

import com.battleship.field.Board;
import com.battleship.io.ConsoleIO;
import com.battleship.placement.ManualShipPlacementConsole;
import com.battleship.player.Player;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SinglePlayerGame implements Game{

    private static final String BOT_NAME = "Ботярик";

    private final ConsoleIO consoleIO;
    private final ManualShipPlacementConsole manualShipPlacementConsole;

    @Override
    public void start(Player player) {
        Player bot = new Player(BOT_NAME);

        Board playerBoard = new Board();
        Board botBoard = new Board();

        consoleIO.printEmptyLine();
        consoleIO.printLine(player.name() + ", выбран режим: одиночная игра");
        consoleIO.printLine("Ваш соперник: " + bot.name());
        consoleIO.printLine("Размер поля: " + playerBoard.size() + "x" + playerBoard.size());
        consoleIO.printLine("Подготовка игры началась");

        manualShipPlacementConsole.placeShips(player, playerBoard);

        consoleIO.printEmptyLine();
        consoleIO.printLine("Расстановка кораблей игрока завершена");
        consoleIO.printLine("Автоматическую расстановку кораблей бота добавим следующим этапом.");
    }
}
