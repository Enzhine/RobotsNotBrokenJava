package ru.enzhine.rnb.world.entity.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class BoundingBox {
    private final double x;
    private final double y;
    private final int pxWidth;
    private final int pxHeight;

    public BoundingBox translated(double dx, double dy) {
        return new BoundingBox(x + dx, y + dy, pxWidth, pxHeight);
    }
}
