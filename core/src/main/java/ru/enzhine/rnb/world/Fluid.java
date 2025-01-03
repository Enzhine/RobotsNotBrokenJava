package ru.enzhine.rnb.world;

import lombok.Getter;

@Getter
public enum Fluid {
    OIL(1),
    WATER(0);

    private final int id;

    Fluid(int id) {
        this.id = id;
    }

    public static Fluid getById(int id) {
        return switch (id) {
            case 1 -> OIL;
            case 0 -> WATER;
            default -> throw new IllegalArgumentException(String.format("Unknown id %d", id));
        };
    }
}
