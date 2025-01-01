package ru.enzhine.rnb.world;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocationTest {

    private World w = new WorldImpl(10, 42, .2f, .2f);
    private Chunk c = new ChunkImpl(w, 0, 0);

    @Test
    public void instancing_blockCoords_correct() {
        var location = new Location(0.1d, 0.1d, c);

        Assertions.assertEquals(0L, location.getBlockX());
        Assertions.assertEquals(0L, location.getBlockY());

        location = new Location(-0.1d, -0.1d, c);

        Assertions.assertEquals(-1L, location.getBlockX());
        Assertions.assertEquals(-1L, location.getBlockY());
    }
}
