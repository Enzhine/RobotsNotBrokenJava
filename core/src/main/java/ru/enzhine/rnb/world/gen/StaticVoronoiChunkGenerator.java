package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.World;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockFactory;

import java.util.List;

public class StaticVoronoiChunkGenerator implements ChunkGenerator {

    private final List<WorldBiome> biomes;
    private final World world;
    private BlockFactory blockFactory;

    public StaticVoronoiChunkGenerator(World world, BlockFactory blockFactory, List<WorldBiome> biomes) {
        this.blockFactory = blockFactory;
        this.world = world;
        this.biomes = biomes;
    }

    private Long chunkGlobalX(Chunk c, Long offset) {
        return c.getOffsetX() * chunkSize() + offset;
    }

    private Long chunkGlobalY(Chunk c, Long offset) {
        return c.getOffsetY() * chunkSize() + offset;
    }

    private int chunkSize() {
        return world.chunkSize();
    }

    private Double dist(long x1, long y1, long x2, long y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private WorldBiome getClosestBiome(long x, long y) {
        double min = Double.MAX_VALUE;
        WorldBiome res = null;
        for (WorldBiome b : biomes) {
            double dist = dist(x, y, b.getX(), b.getY());
            if (dist < min) {
                min = dist;
                res = b;
            }
        }
        return res;
    }

    @Override
    public void fillChunk(Chunk c) {
        int cSize = chunkSize();
        for (int x = 0; x < cSize; ++x) {
            for (int y = 0; y < cSize; ++y) {
                long blockX = chunkGlobalX(c, (long) x);
                long blockY = chunkGlobalY(c, (long) y);
                BiomeType biome = getClosestBiome(blockX, blockY).getBiomeType();

                c.set(blockFactory.makeBlock(blockTypeByBiome(biome), blockX, blockY, biome, c));
            }
        }
    }

    BlockType blockTypeByBiome(BiomeType biomeType) {
        return switch (biomeType) {
            case RANDOM -> BlockType.DIRT;
            case RANDOM1 -> BlockType.STONE;
        };
    }

    @Override
    public BiomeType getBiome(long x, long y) {
        return getClosestBiome(x, y).getBiomeType();
    }

    @Override
    public void setBlockFactory(BlockFactory bf) {
        blockFactory = bf;
    }

    @Override
    public BlockFactory getBlockFactory() {
        return blockFactory;
    }

    public List<WorldBiome> getBiomes() {
        return biomes;
    }
}
