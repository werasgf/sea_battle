package com.battleship;

import com.battleship.app.GameApplication;
import com.battleship.bot.*;
import com.battleship.field.BoardRenderer;
import com.battleship.field.BoardsPairRenderer;
import com.battleship.field.CoordinateParser;
import com.battleship.game.Game;
import com.battleship.game.SinglePlayerGame;
import com.battleship.io.ConsoleIO;
import com.battleship.menu.GameModeSelector;
import com.battleship.placement.AutoShipPlacementService;
import com.battleship.placement.ManualShipPlacementConsole;
import com.battleship.placement.ShipPlacementModeSelector;
import com.battleship.ship.ShipPlacementCommandParser;
import com.battleship.ship.ShipPlacementService;
import com.battleship.turn.PlayerTurnService;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ConsoleIO consoleIO = new ConsoleIO(
                new Scanner(System.in),
                System.out
        );

        BoardRenderer boardRenderer = new BoardRenderer(consoleIO);
        CoordinateParser coordinateParser = new CoordinateParser();
        BoardsPairRenderer boardsPairRenderer = new BoardsPairRenderer(consoleIO);

        PlayerTurnService playerTurnService = new PlayerTurnService(
                consoleIO,
                coordinateParser,
                boardsPairRenderer
        );

        ShipPlacementCommandParser commandParser = new ShipPlacementCommandParser(coordinateParser);
        ShipPlacementService shipPlacementService = new ShipPlacementService();

        ManualShipPlacementConsole manualShipPlacementConsole = new ManualShipPlacementConsole(
                consoleIO,
                boardRenderer,
                commandParser,
                shipPlacementService
        );

        AutoShipPlacementService autoShipPlacementService = new AutoShipPlacementService(
                shipPlacementService
        );

        ShipPlacementModeSelector shipPlacementModeSelector = new ShipPlacementModeSelector(
                consoleIO
        );

        GameModeSelector gameModeSelector = new GameModeSelector(consoleIO);

        Random random = new Random();

        BotTargetMemory botTargetMemory = new BotTargetMemory();

        RandomSearchStrategy randomSearchStrategy = new RandomSearchStrategy(random);

        AttackFinishingStrategy attackFinishingStrategy = new AttackFinishingStrategy();

        BotTurnService botTurnService = new BotTurnService(
                consoleIO,
                randomSearchStrategy,
                attackFinishingStrategy,
                botTargetMemory
        );

        Game singlePlayerGame = new SinglePlayerGame(
                consoleIO,
                manualShipPlacementConsole,
                autoShipPlacementService,
                boardRenderer,
                shipPlacementModeSelector,
                playerTurnService,
                botTurnService
        );

        GameApplication application = new GameApplication(
                consoleIO,
                gameModeSelector,
                singlePlayerGame
        );

        application.run();
    }
}