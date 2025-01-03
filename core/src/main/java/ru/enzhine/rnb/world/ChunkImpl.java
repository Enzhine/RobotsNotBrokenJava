package ru.enzhine.rnb.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.render.Rendering;
import ru.enzhine.rnb.utils.adt.Placeable2D;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.block.base.Ticking;
import ru.enzhine.rnb.world.entity.Entity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ChunkImpl implements Chunk, Placeable2D, Rendering {
    final long chunkX;
    final long chunkY;
    boolean loaded = true;
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
        if (localX < 0 || localX >= chunkSize) {
            throw new IndexOutOfBoundsException(String.format("localX index must be withing %d and exclusive %d", 0, chunkSize));
        }
        if (localY < 0 || localY >= chunkSize) {
            throw new IndexOutOfBoundsException(String.format("localY index must be withing %d and exclusive %d", 0, chunkSize));
        }
        return blockLayer[localY * this.chunkSize + localX];
    }

    byte getLightByLocal(int localX, int localY) {
        if (localX < 0 || localX >= chunkSize) {
            throw new IndexOutOfBoundsException(String.format("localX index must be withing %d and exclusive %d", 0, chunkSize));
        }
        if (localY < 0 || localY >= chunkSize) {
            throw new IndexOutOfBoundsException(String.format("localY index must be withing %d and exclusive %d", 0, chunkSize));
        }
        return lightLayer[localY * this.chunkSize + localX];
    }

    void setBlockByLocal(Block b, int localX, int localY) {
        if (localX < 0 || localX >= chunkSize) {
            throw new IndexOutOfBoundsException(String.format("localX index must be withing %d and exclusive %d", 0, chunkSize));
        }
        if (localY < 0 || localY >= chunkSize) {
            throw new IndexOutOfBoundsException(String.format("localY index must be withing %d and exclusive %d", 0, chunkSize));
        }
        blockLayer[localY * this.chunkSize + localX] = b;
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
    public Long getGlobalX() {return chunkX * chunkSize;}

    @Override
    public Long getOffsetY() {
        return chunkY;
    }

    @Override
    public Long getGlobalY() {return chunkY * chunkSize;}

    @Override
    public void set(Block b) {
        int lX = globalToLocalXY(b.getLocation().getBlockX());
        int lY = globalToLocalXY(b.getLocation().getBlockY());
        setBlockByLocal(b, lX, lY);
    }

    @Override
    public Block get(int localX, int localY) {
        try {
            return getBlockByLocal(localX, localY);
        }catch (IndexOutOfBoundsException ex) {
            return null;
        }
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
    public void batchRender(SpriteBatch batch, Viewport viewport) {
        for (int i = 0; i < blockLayer.length; i++) {
            Block b = blockLayer[i];
            float tint = colorOfLevel(lightLayer[i]);

            if (b instanceof Rendering) {
                Color prev = batch.getColor();
                batch.setColor(tint, tint, tint, 1f);
                ((Rendering) b).batchRender(batch, viewport);
                batch.setColor(prev);
            }
        }
    }

    @Override
    public void shapeRender(ShapeRenderer renderer, Viewport viewport) {
        for (int i = 0; i < blockLayer.length; i++) {
            Block b = blockLayer[i];
            if (b instanceof Rendering) {
                ((Rendering) b).shapeRender(renderer, viewport);
            }
        }
    }

    @Override
    public void onTick() {
        for (Block b : blockLayer) {
            if (b instanceof Ticking) {
                ((Ticking) b).onTick();
            }
        }
    }
}
