package com.battleship.menu;

import com.battleship.io.ConsoleIO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameModeSelector {

    private final ConsoleIO consoleIO;

    public GameMode select() {
        while (true) {
            printMenu();

            int selectedNumber = consoleIO.readInt("Ваш выбор: ");

            if (selectedNumber == GameMode.SINGLE_PLAYER.getNumber()) {
                return GameMode.SINGLE_PLAYER;
            }

            if (selectedNumber == GameMode.TWO_PLAYERS.getNumber()) {
                consoleIO.printLine("Режим игры с напарником пока недоступен.");
                consoleIO.printLine("Пожалуйста, выберите одиночную игру.");
                consoleIO.printEmptyLine();
                continue;
            }

            consoleIO.printLine("Неизвестный режим игры. Попробуйте ещё раз.");
            consoleIO.printEmptyLine();
        }
    }

    private void printMenu() {
        consoleIO.printLine("Выберите режим игры:");
        consoleIO.printLine("1 — " + GameMode.SINGLE_PLAYER.getTitle());
        consoleIO.printLine("2 — " + GameMode.TWO_PLAYERS.getTitle());
        consoleIO.printEmptyLine();
    }
}
