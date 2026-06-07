package com.battleship.game;

import com.battleship.io.ConsoleIO;
import com.battleship.player.Player;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SinglePlayerGame implements Game{

    private static final String BOT_NAME = "Ботярик";

    private final ConsoleIO consoleIO;

    @Override
    public void start(Player player) {
        Player bot = new Player(BOT_NAME);

        consoleIO.printEmptyLine();
        consoleIO.printLine(player.name() + ", выбран режим: одиночная игра");
        consoleIO.printLine("Ваш соперник: " + bot.name());
        consoleIO.printLine("Подготовка игры завершена");
        consoleIO.printLine("Дальше будет реализована расстановка кораблей");
    }
}
