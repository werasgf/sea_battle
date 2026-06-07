package com.battleship.field;

import java.util.Locale;
import java.util.Optional;

public enum Direction {
    HORIZONTAL,
    VERTICAL;

    public static Optional<Direction> fromConsoleValue(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }

        String normalizedValue = value.trim().toUpperCase(Locale.ROOT);

        return switch (normalizedValue) {
            case "H", "Г", "HOR", "HORIZONTAL", "ГОРИЗОНТАЛЬНО" -> Optional.of(HORIZONTAL);
            case "V", "В", "VER", "VERTICAL", "ВЕРТИКАЛЬНО" -> Optional.of(VERTICAL);
            default -> Optional.empty();
        };
    }
}
