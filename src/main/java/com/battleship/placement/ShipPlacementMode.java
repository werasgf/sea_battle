package com.battleship.placement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ShipPlacementMode {
    MANUAL(1, "Ручная расстановка"),
    AUTO(2, "Автоматическая расстановка");

    private final int number;
    private final String title;
}
