package ru.enzhine.rnb.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.render.Rendering;
import ru.enzhine.rnb.utils.Placeable2D;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.entity.Entity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ChunkImpl implements Chunk, Placeable2D, Rendering {
    final long chunkX;
    final long chunkY;
    boolean loaded = false;
    final World world;

    List<Entity> entities = new LinkedList<>();
    final int chunkSize;
    final Block[] blockLayer;
    final byte[] lightLayer;

    public ChunkImpl(World w, long cX, long cY){
        this.chunkX = cX;
        this.chunkY = cY;
        this.chunkSize = w.chunkSize();
        this.world = w;
        blockLayer = new Block[this.chunkSize * this.chunkSize];
        lightLayer = new byte[this.chunkSize * this.chunkSize];
        Arrays.fill(lightLayer, MAX_LIGHT_LEVEL);
    }

    Block getBlockByLocal(int localX, int localY) {
        return blockLayer[localX * this.chunkSize + localY];
    }

    byte getLightByLocal(int localX, int localY) {
        return lightLayer[localX * this.chunkSize + localY];
    }

    void setBlockByLocal(Block b, int localX, int localY) {
        blockLayer[localX * this.chunkSize + localY] = b;
    }

    int globalToLocalXY(long xy) {
        int res = (int) (xy % chunkSize);
        if(res < 0){
            res += chunkSize;
        }
        return res;
    }

    @Override
    public Long getX() {
        return getOffsetX();
    }

    @Override
    public Long getY() {
        return getOffsetY();
    }

    @Override
    public Long getOffsetX() {
        return chunkX;
    }

    @Override
    public Long getOffsetY() {
        return chunkY;
    }

    @Override
    public void set(Block b) {
        int lX = globalToLocalXY(b.getLocation().getBlockX());
        int lY = globalToLocalXY(b.getLocation().getBlockY());
        setBlockByLocal(b, lX, lY);
    }

    @Override
    public Block get(int localX, int localY) {
        return getBlockByLocal(localX, localY);
    }

    @Override
    public byte getLightLevel(int localX, int localY) {
        return getLightByLocal(localX, localY);
    }

    @Override
    public ListIterator<Entity> getEntities() {
        return entities.listIterator();
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    float colorOfLevel(byte level){
        return (float) level / MAX_LIGHT_LEVEL;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        for (int i = 0; i < blockLayer.length; i++) {
            Block b = blockLayer[i];
            float tint = colorOfLevel(lightLayer[i]);

            if (b instanceof Rendering) {
                Color prev = batch.getColor();
                batch.setColor(tint, tint, tint, 1f);
                ((Rendering) b).render(batch, viewport);
                batch.setColor(prev);
            }
        }
    }
}
