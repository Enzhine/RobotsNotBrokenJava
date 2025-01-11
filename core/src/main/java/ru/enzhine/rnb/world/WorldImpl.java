package ru.enzhine.rnb.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.utils.ImmutablePair;
import ru.enzhine.rnb.utils.MathUtils;
import ru.enzhine.rnb.world.block.base.BlockFactory;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.Rendering;
import ru.enzhine.rnb.utils.adt.Map2D;
import ru.enzhine.rnb.utils.adt.TreeMap2D;
import ru.enzhine.rnb.world.block.base.BlockFactoryImpl;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.entity.base.Entity;
import ru.enzhine.rnb.world.entity.base.EntityFactory;
import ru.enzhine.rnb.world.entity.base.EntityFactoryImpl;
import ru.enzhine.rnb.world.entity.base.EntityType;
import ru.enzhine.rnb.world.gen.*;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class WorldImpl implements World, Rendering {

    private final int chunkSize;
    private final long seed;
    private final Map2D<ChunkImpl> gameMap;
    private final ChunkGenerator chunkGen;
    private final BlockFactory blockFactory;
    private final EntityFactory entityFactory;

    public WorldImpl(int chunkSize, long seed, float biomesPerChunk, float gapProbability) {
        this.chunkSize = chunkSize;
        this.seed = seed;
        this.gameMap = new TreeMap2D<>();
        this.blockFactory = new BlockFactoryImpl();
        this.entityFactory = new EntityFactoryImpl();
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
    public Entity summonEntity(EntityType type, double x, double y) {
        Chunk c = getChunk((long) x, (long) y, true);
        Entity e = this.entityFactory.makeEntity(type, x, y, c);
        c.addEntity(e);
        return e;
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

    private ImmutablePair<Long, Long> getTopLeftWorldChunkCoords(Viewport viewport, ShapeDrawer drawer) {
        var camera = (OrthographicCamera) viewport.getCamera();
        var proj = camera.unproject(new Vector3(-1f, -1f, 0));

        var blockX = MathUtils.blockPos(proj.x / BLOCK_PIXEL_SIZE);
        var blockY = MathUtils.blockPos(proj.y / BLOCK_PIXEL_SIZE);

        return ImmutablePair.of(chunkOffset(blockX), chunkOffset(blockY));
    }

    private ImmutablePair<Long, Long> getBottomRightWorldChunkCoords(Viewport viewport, ShapeDrawer drawer) {
        var camera = (OrthographicCamera) viewport.getCamera();
        var proj = camera.unproject(new Vector3(viewport.getScreenWidth(), viewport.getScreenHeight(), 0));

        var blockX = MathUtils.blockPos(proj.x / BLOCK_PIXEL_SIZE);
        var blockY = MathUtils.blockPos(proj.y / BLOCK_PIXEL_SIZE);

        return ImmutablePair.of(chunkOffset(blockX), chunkOffset(blockY));
    }

    @Override
    public void render(SpriteBatch batch, ShapeDrawer drawer, Viewport viewport) {
        var topLeft = getTopLeftWorldChunkCoords(viewport, drawer);
        var bottomRight = getBottomRightWorldChunkCoords(viewport, drawer);

        for (ChunkImpl chunk : gameMap.withinBounds(topLeft.getLeft(), bottomRight.getLeft(), bottomRight.getRight(), topLeft.getRight())) {
            chunk.render(batch, drawer, viewport);
        }
    }

    @Override
    public void onTick() {
        for (ChunkImpl chunk : gameMap) {
            chunk.onTick();
        }
    }
}
