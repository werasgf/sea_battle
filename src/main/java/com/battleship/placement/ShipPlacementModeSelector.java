package com.battleship.placement;

import com.battleship.io.ConsoleIO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShipPlacementModeSelector {

    private final ConsoleIO consoleIO;

    public ShipPlacementMode select() {
        while (true) {
            printMenu();

            int selectedNumber = consoleIO.readInt("Ваш выбор: ");

            if (selectedNumber == ShipPlacementMode.MANUAL.getNumber()) {
                return ShipPlacementMode.MANUAL;
            }

            if (selectedNumber == ShipPlacementMode.AUTO.getNumber()) {
                return ShipPlacementMode.AUTO;
            }

            consoleIO.printLine("Неизвестный вариант расстановки. Попробуйте ещё раз");
            consoleIO.printEmptyLine();
        }
    }

    private void printMenu() {
        consoleIO.printLine("Выберите способ расстановки кораблей:");
        consoleIO.printLine("1 — " + ShipPlacementMode.MANUAL.getTitle());
        consoleIO.printLine("2 — " + ShipPlacementMode.AUTO.getTitle());
        consoleIO.printEmptyLine();
    }
}
