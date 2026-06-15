package com.battleship.turn;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShotResult {

    MISS("мимо"),
    HIT("ранил"),
    KILLED("убил"),
    ALREADY_SHOT("по этой клетке уже стреляли");

    private final String title;
}
