package com.battleship.app;

import com.battleship.game.Game;
import com.battleship.io.ConsoleIO;
import com.battleship.menu.GameMode;
import com.battleship.menu.GameModeSelector;
import com.battleship.player.Player;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameApplication {

    private final ConsoleIO consoleIO;
    private final GameModeSelector gameModeSelector;
    private final Game singlePlayerGame;

    public void run() {
        consoleIO.printLine("Морской бой");
        consoleIO.printEmptyLine();

        String playerName = consoleIO.readRequiredLine("Введите имя игрока: ");
        Player player = new Player(playerName);

        consoleIO.printEmptyLine();

        GameMode selectedMode = gameModeSelector.select();

        if (selectedMode == GameMode.SINGLE_PLAYER) {
            singlePlayerGame.start(player);
        }
    }
}
