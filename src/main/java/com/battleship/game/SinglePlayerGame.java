package com.battleship.game;

import com.battleship.bot.BotTurnService;
import com.battleship.bot.KnowledgeBoard;
import com.battleship.field.Board;
import com.battleship.field.BoardRenderer;
import com.battleship.io.ConsoleIO;
import com.battleship.placement.AutoShipPlacementService;
import com.battleship.placement.ManualShipPlacementConsole;
import com.battleship.placement.ShipPlacementMode;
import com.battleship.placement.ShipPlacementModeSelector;
import com.battleship.player.Player;
import com.battleship.turn.PlayerTurnService;
import com.battleship.turn.ShotResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SinglePlayerGame implements Game {

    private static final String BOT_NAME = "Ботярик";

    private final ConsoleIO consoleIO;
    private final ManualShipPlacementConsole manualShipPlacementConsole;
    private final AutoShipPlacementService autoShipPlacementService;
    private final BoardRenderer boardRenderer;
    private final ShipPlacementModeSelector shipPlacementModeSelector;
    private final PlayerTurnService playerTurnService;
    private final BotTurnService botTurnService;

    @Override
    public void start(Player player) {
        Player bot = new Player(BOT_NAME);

        Board playerBoard = new Board();
        Board botBoard = new Board();

        consoleIO.printEmptyLine();
        consoleIO.printLine(player.name() + ", выбран режим: одиночная игра");
        consoleIO.printLine("Ваш соперник: " + bot.name());

        placePlayerShips(player, playerBoard);
        placeBotShips(botBoard);

        KnowledgeBoard playerKnowledgeBoard = new KnowledgeBoard();
        KnowledgeBoard botKnowledgeBoard = new KnowledgeBoard();

        consoleIO.printEmptyLine();
        consoleIO.printLine("""
                Игра началась\s
                Координаты ввода, должны иметь подобную структуру:\s
                a1, где а - буква координаты, 1 - цифра координаты""");

        runGameLoop(
                player,
                bot,
                playerBoard,
                botBoard,
                playerKnowledgeBoard,
                botKnowledgeBoard
        );
    }

    private void runGameLoop(
            Player player,
            Player bot,
            Board playerBoard,
            Board botBoard,
            KnowledgeBoard playerKnowledgeBoard,
            KnowledgeBoard botKnowledgeBoard
    ) {
        boolean playerTurn = true;

        while (!playerBoard.allShipsKilled() && !botBoard.allShipsKilled()) {
            if (playerTurn) {
                ShotResult playerShotResult = playerTurnService.makeTurn(
                        player,
                        playerBoard,
                        botBoard,
                        playerKnowledgeBoard
                );

                if (botBoard.allShipsKilled()) {
                    consoleIO.printEmptyLine();
                    consoleIO.printLine("Победа! Русские вперед!");
                    return;
                }

                playerTurn = playerShotResult != ShotResult.MISS;
            } else {
                ShotResult botShotResult = botTurnService.makeTurn(
                        playerBoard,
                        botKnowledgeBoard
                );

                if (playerBoard.allShipsKilled()) {
                    consoleIO.printEmptyLine();
                    consoleIO.printLine("Поражение. Ты проиграл битву, но не войну!");
                    consoleIO.printEmptyLine();
                    consoleIO.printLine("Расстановка кораблей соперника:");
                    boardRenderer.render(botBoard);
                    return;
                }

                playerTurn = botShotResult == ShotResult.MISS;
            }
        }
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
        consoleIO.printLine("Корабли игрока расставлены автоматически.");
        consoleIO.printLine("Ваше поле:");
        boardRenderer.render(playerBoard);
    }

    private void placeBotShips(Board botBoard) {
        autoShipPlacementService.placeShips(botBoard);

        consoleIO.printEmptyLine();
        consoleIO.printLine("Корабли соперника расставлены автоматически.");
        consoleIO.printLine("Поле соперника скрыто.");
    }
}