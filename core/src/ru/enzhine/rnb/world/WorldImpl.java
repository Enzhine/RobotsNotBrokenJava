package ru.enzhine.rnb.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.enzhine.rnb.render.Rendering;
import ru.enzhine.rnb.utils.Map2D;
import ru.enzhine.rnb.utils.TreeMap2D;
import ru.enzhine.rnb.world.block.BlockFactoryImpl;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.Textures;
import ru.enzhine.rnb.world.gen.ChunkGenerator;
import ru.enzhine.rnb.world.gen.DynamicVoronoiChunkGenerator;
import ru.enzhine.rnb.world.gen.StaticVoronoiChunkGenerator;
import ru.enzhine.rnb.world.gen.WorldBiome;

import java.util.List;
import java.util.stream.IntStream;

public class WorldImpl implements World, Rendering {

    private final int chunkSize;
    private final Map2D<ChunkImpl> gameMap = new TreeMap2D<>();
//    private List<WorldBiome> bbb = IntStream.iterate(0, i -> i < 10, i -> i + 1).mapToObj(i -> {
//        return new WorldBiome(Math.random() > 0.5 ? BiomeType.RANDOM : BiomeType.RANDOM1, (long)(Math.random() * 30 - 15), (long)(Math.random() * 30 - 15));
//    }).toList();
//    private final ChunkGenerator chunkGen = new StaticVoronoiChunkGenerator(this, new BlockFactoryImpl(), bbb);
    private final ChunkGenerator chunkGen = new DynamicVoronoiChunkGenerator(0.5f, this, new BlockFactoryImpl());

    public WorldImpl(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public long chunkOffset(Long xy){
        return xy / chunkSize;
    }

    public int chunkLocal(Long xy){
        return (int) (xy % chunkSize);
    }

    @Override
    public void setBlock(BlockType type, Long x, Long y) {
        Chunk c = getChunk(x, y);
        BiomeType bt = chunkGen.getBiome(x, y);
        Block b = chunkGen.getBlockFactory().makeBlock(type, x, y, bt, c);
        c.set(b);
    }

    @Override
    public Block getBlock(Long x, Long y) {
        Chunk c = getChunk(x, y);
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
    public Chunk getChunk(Long x, Long y) {
        Chunk c = gameMap.at(chunkOffset(x), chunkOffset(y));
        if(c == null){
            c = genChunkAt(chunkOffset(x), chunkOffset(y));
        }
        return c;
    }

    @Override
    public int chunkSize() {
        return chunkSize;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        long x = (long) viewport.getCamera().position.x;
        long y = (long) viewport.getCamera().position.y;
        long xMin = (x - (long)viewport.getWorldWidth() / 2) / BLOCK_PIXEL_SIZE / chunkSize - 1;
        long xMax = (x + (long)viewport.getWorldWidth() / 2) / BLOCK_PIXEL_SIZE / chunkSize + 1;
        long yMin = (y - (long)viewport.getWorldHeight() / 2) / BLOCK_PIXEL_SIZE / chunkSize - 1;
        long yMax = (y + (long)viewport.getWorldHeight() / 2) / BLOCK_PIXEL_SIZE / chunkSize + 1;
        for (ChunkImpl chunk : gameMap.withinBounds(xMin, xMax, yMin, yMax)) {
            chunk.render(batch, viewport);
        }
        // TODO: DEBUG CODE
        for (WorldBiome biome : ((DynamicVoronoiChunkGenerator) chunkGen).getBiomes()) {
            batch.draw(Textures.getTexture("blocks/sandstone.png"), biome.getX() * 16, biome.getY() * 16);
        }
    }
}
