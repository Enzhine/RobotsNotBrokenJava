package ru.enzhine.rnb.world;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class BoundingBox {
    private final double x; // left bottom
    private final double y; // left bottom
    private final int pxWidth;
    private final int pxHeight;

    public BoundingBox translated(double dx, double dy) {
        return new BoundingBox(x + dx, y + dy, pxWidth, pxHeight);
    }

    public double leftX() {
        return this.x;
    }

    public double rightX() {
        return this.x + (double) pxHeight / WorldImpl.BLOCK_PIXEL_SIZE;
    }

    public double topY() {
        return this.y + (double) pxHeight / WorldImpl.BLOCK_PIXEL_SIZE;
    }

    public double bottomY() {
        return this.y;
    }

    public double leftXPx() {
        return this.x * WorldImpl.BLOCK_PIXEL_SIZE;
    }

    public double rightXPx() {
        return this.x * WorldImpl.BLOCK_PIXEL_SIZE + pxHeight;
    }

    public double topYPx() {
        return this.y * WorldImpl.BLOCK_PIXEL_SIZE + pxHeight;
    }

    public double bottomYPx() {
        return this.y * WorldImpl.BLOCK_PIXEL_SIZE;
    }

    public boolean collides(double xPx, double yPx) {
        return leftXPx() <= xPx && xPx <= rightXPx() && bottomYPx() <= yPx && yPx <= topYPx();
    }


}
