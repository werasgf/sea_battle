package com.battleship.placement;

public enum ShipPlacementMode {
    MANUAL(1, "Ручная расстановка"),
    AUTO(2, "Автоматическая расстановка");

    private final int number;
    private final String title;

    ShipPlacementMode(int number, String title) {
        this.number = number;
        this.title = title;
    }

    public int number() {
        return number;
    }

    public String title() {
        return title;
    }
}
