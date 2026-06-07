package com.battleship.io;

import lombok.RequiredArgsConstructor;

import java.io.PrintStream;
import java.util.Scanner;

@RequiredArgsConstructor
public class ConsoleIO {

    private final Scanner scanner;
    private final PrintStream output;

    public void printLine(String message) {
        output.println(message);
    }

    public void printEmptyLine() {
        output.println();
    }

    public String readLine(String prompt) {
        output.print(prompt);
        return scanner.nextLine().trim();
    }

    public String readRequiredLine(String prompt) {
        while (true) {
            String value = readLine(prompt);

            if (!value.isBlank()) {
                return value;
            }

            printLine("Значение не может быть пустым. Попробуйте ещё раз.");
        }
    }

    public int readInt(String prompt) {
        while (true) {
            String value = readLine(prompt);

            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                printLine("Введите число.");
            }
        }
    }
}
