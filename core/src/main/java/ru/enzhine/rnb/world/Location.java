package ru.enzhine.rnb.world;

import lombok.ToString;
import ru.enzhine.rnb.utils.MathUtils;
import ru.enzhine.rnb.world.block.base.Block;

@ToString
public class Location {
    private final Double x;
    private final Double y;
    private final long blockX;
    private final long blockY;
    private final int localX;
    private final int localY;

    private final Chunk c;

    public Location(Double x, Double y, Chunk c) {
        this.x = x;
        this.y = y;
        this.c = c;
        if (x > 0) {
            this.blockX = x.longValue();
        } else {
            this.blockX = (long) Math.floor(x);
        }
        if (y > 0) {
            this.blockY = y.longValue();
        } else {
            this.blockY = (long) Math.floor(y);
        }
        this.localX = globalToLocalXY(this.blockX);
        this.localY = globalToLocalXY(this.blockY);
    }

    int globalToLocalXY(long xy) {
        var chunkSize = c.getWorld().chunkSize();
        int res = (int) (xy % chunkSize);
        if (res < 0) {
            res += chunkSize;
        }
        return res;
    }

    public Double getX() {
        return this.x;
    }

    public Long getBlockX() {
        return this.blockX;
    }

    public int getChunkLocalX() {
        return localX;
    }

    public Double getY() {
        return this.y;
    }

    public Long getBlockY() {
        return this.blockY;
    }

    public int getChunkLocalY() {
        return localY;
    }

    public World getWorld() {
        return this.c.getWorld();
    }

    public Chunk getChunk() {
        return this.c;
    }

    public Block getBlock() {
        return this.c.get(localX, localY);
    }

    public Location translated(double dx, double dy) {
        double newX = this.x + dx;
        double newY = this.y + dy;
        Chunk newC = c.contains(newX, newY) ? c : getWorld().getChunk(MathUtils.blockPos(newX), MathUtils.blockPos(newY), true);

        return new Location(newX, newY, newC);
    }
}
