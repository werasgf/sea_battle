package com.battleship.player;

public record Player(String name) {

    public Player(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя игрока не может быть пустым");
        }

        this.name = name.trim();
    }
}
