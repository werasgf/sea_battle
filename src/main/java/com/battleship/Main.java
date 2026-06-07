package com.battleship;

import com.battleship.app.GameApplication;
import com.battleship.field.BoardRenderer;
import com.battleship.game.Game;
import com.battleship.game.SinglePlayerGame;
import com.battleship.io.ConsoleIO;
import com.battleship.menu.GameModeSelector;
import com.battleship.placement.ManualShipPlacementConsole;
import com.battleship.ship.ShipPlacementCommandParser;
import com.battleship.ship.ShipPlacementService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ConsoleIO consoleIO = new ConsoleIO(
                new Scanner(System.in),
                System.out
        );

        BoardRenderer boardRenderer = new BoardRenderer(consoleIO);
        ShipPlacementCommandParser commandParser = new ShipPlacementCommandParser();
        ShipPlacementService shipPlacementService = new ShipPlacementService();

        ManualShipPlacementConsole manualShipPlacementConsole = new ManualShipPlacementConsole(
                consoleIO,
                boardRenderer,
                commandParser,
                shipPlacementService
        );

        GameModeSelector gameModeSelector = new GameModeSelector(consoleIO);
        Game singlePlayerGame = new SinglePlayerGame(consoleIO, manualShipPlacementConsole);

        GameApplication application = new GameApplication(
                consoleIO,
                gameModeSelector,
                singlePlayerGame
        );

        application.run();
    }
}