package com.battleship;

import com.battleship.app.GameApplication;
import com.battleship.game.Game;
import com.battleship.game.SinglePlayerGame;
import com.battleship.io.ConsoleIO;
import com.battleship.menu.GameModeSelector;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ConsoleIO consoleIO = new ConsoleIO(
                new Scanner(System.in),
                System.out
        );

        GameModeSelector gameModeSelector = new GameModeSelector(consoleIO);
        Game singlePlayerGame = new SinglePlayerGame(consoleIO);

        GameApplication application = new GameApplication(
                consoleIO,
                gameModeSelector,
                singlePlayerGame
        );

        application.run();
    }
}