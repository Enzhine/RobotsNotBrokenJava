package ru.enzhine.rnb.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.utils.MathUtils;
import ru.enzhine.rnb.world.block.base.BlockFactory;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.Rendering;
import ru.enzhine.rnb.utils.adt.Map2D;
import ru.enzhine.rnb.utils.adt.TreeMap2D;
import ru.enzhine.rnb.world.block.base.BlockFactoryImpl;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.gen.*;

public class WorldImpl implements World, Rendering {

    private final int chunkSize;
    private final long seed;
    private final Map2D<ChunkImpl> gameMap;
    private final ChunkGenerator chunkGen;
    private final BlockFactory blockFactory;

    public WorldImpl(int chunkSize, long seed, float biomesPerChunk, float gapProbability) {
        this.chunkSize = chunkSize;
        this.seed = seed;
        this.gameMap = new TreeMap2D<>();
        this.blockFactory = new BlockFactoryImpl();
        this.chunkGen = new LazyDeterminedVoronoiChunkGenerator(
                this,
                new WorldBiomeFactoryImpl(),
                new BlockGeneratorImpl(this.blockFactory),
                new BiomeGeneratorImpl(),
                new OreProcessorGeneratorImpl(this.blockFactory),
                this.seed,
                biomesPerChunk,
                gapProbability
        );
    }

    public long chunkOffset(long xy) {
        var result = (double) xy / chunkSize;
        return result > 0 ? (long) result : (long) Math.floor(result);
    }

    public int chunkLocal(long xy) {
        return MathUtils.remainder(xy, chunkSize);
    }

    @Override
    public void setBlock(BlockType type, Long x, Long y) {
        Chunk c = getChunk(x, y, true);
        BiomeType bt = chunkGen.getBiome(x, y);
        Block b = this.blockFactory.makeBlock(type, x, y, bt, c);
        c.set(b);
    }

    @Override
    public Block getBlock(Long x, Long y, boolean loadChunk) {
        Chunk c = getChunk(x, y, loadChunk);
        if (c == null) {
            return null;
        }
        return c.get(chunkLocal(x), chunkLocal(y));
    }

    private Chunk genChunkAt(Long cx, Long cy) {
        ChunkImpl c = new ChunkImpl(this, cx, cy);
        gameMap.put(c);
        chunkGen.fillChunk(c);
        return c;
    }

    public void genRangeChunks(Long cx1, Long cx2, Long cy1, Long cy2) {
        for (long x = cx1; x <= cx2; x++) {
            for (long y = cy1; y <= cy2; y++) {
                genChunkAt(x, y);
            }
        }
    }

    @Override
    public Chunk getChunk(Long x, Long y, boolean loadChunk) {
        Chunk c = gameMap.at(chunkOffset(x), chunkOffset(y));
        if (c == null && loadChunk) {
            c = genChunkAt(chunkOffset(x), chunkOffset(y));
        }
        return c;
    }

    @Override
    public int chunkSize() {
        return chunkSize;
    }

    @Override
    public void batchRender(SpriteBatch batch, Viewport viewport) {
        long x = (long) viewport.getCamera().position.x;
        long y = (long) viewport.getCamera().position.y;
        long xMin = (x - (long) viewport.getWorldWidth() / 2) / BLOCK_PIXEL_SIZE / chunkSize - 1;
        long xMax = (x + (long) viewport.getWorldWidth() / 2) / BLOCK_PIXEL_SIZE / chunkSize + 1;
        long yMin = (y - (long) viewport.getWorldHeight() / 2) / BLOCK_PIXEL_SIZE / chunkSize - 1;
        long yMax = (y + (long) viewport.getWorldHeight() / 2) / BLOCK_PIXEL_SIZE / chunkSize + 1;

        for (ChunkImpl chunk : gameMap.withinBounds(xMin, xMax, yMin, yMax)) {
            chunk.batchRender(batch, viewport);
        }
    }

    @Override
    public void shapeRender(ShapeRenderer renderer, Viewport viewport) {
        long x = (long) viewport.getCamera().position.x;
        long y = (long) viewport.getCamera().position.y;
        long xMin = (x - (long) viewport.getWorldWidth() / 2) / BLOCK_PIXEL_SIZE / chunkSize - 1;
        long xMax = (x + (long) viewport.getWorldWidth() / 2) / BLOCK_PIXEL_SIZE / chunkSize + 1;
        long yMin = (y - (long) viewport.getWorldHeight() / 2) / BLOCK_PIXEL_SIZE / chunkSize - 1;
        long yMax = (y + (long) viewport.getWorldHeight() / 2) / BLOCK_PIXEL_SIZE / chunkSize + 1;

        for (ChunkImpl chunk : gameMap.withinBounds(xMin, xMax, yMin, yMax)) {
            chunk.shapeRender(renderer, viewport);
        }
    }

    @Override
    public void onTick() {
        for (ChunkImpl chunk : gameMap) {
            chunk.onTick();
        }
    }
}
