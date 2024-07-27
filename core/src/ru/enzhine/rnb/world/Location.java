package ru.enzhine.rnb.world;

public class Location {
    private final Double x;
    private final Double y;

    private final Chunk c;

    public Location(Double x, Double y, Chunk c) {
        this.x = x;
        this.y = y;
        this.c = c;
    }

    public Double getX() {
        return this.x;
    }

    public Long getBlockX() {
        return this.x.longValue();
    }

    public Double getY() {
        return this.y;
    }

    public Long getBlockY() {
        return this.y.longValue();
    }

    public World getWorld() {
        return this.c.getWorld();
    }

    public Chunk getChunk() {
        return this.c;
    }

    public Location translated(Double x, Double y) {
        Double newX = this.x + x;
        Double newY = this.y + y;
        return new Location(newX, newY, this.getWorld().getChunk(newX.longValue(), newY.longValue()));
    }
}
