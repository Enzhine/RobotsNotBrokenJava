package ru.enzhine.rnb.world.entity.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ru.enzhine.rnb.world.WorldImpl;

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

    public boolean collides(double x, double y) {
        var blX = this.x * WorldImpl.BLOCK_PIXEL_SIZE;
        var blY = this.y * WorldImpl.BLOCK_PIXEL_SIZE;
        var trX = this.x * WorldImpl.BLOCK_PIXEL_SIZE + pxWidth;
        var trY = this.y * WorldImpl.BLOCK_PIXEL_SIZE + pxHeight;
        return blX <= x && x <= trX && blY <= y && y <= trY;
    }
}
