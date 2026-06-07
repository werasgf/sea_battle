package com.battleship.menu;

import lombok.Getter;

@Getter
public enum GameMode {

    SINGLE_PLAYER(1, "Одиночная игра"),
    TWO_PLAYERS(2, "С напарником");

    private final int number;
    private final String title;

    GameMode(int number, String title) {
        this.number = number;
        this.title = title;
    }

}
