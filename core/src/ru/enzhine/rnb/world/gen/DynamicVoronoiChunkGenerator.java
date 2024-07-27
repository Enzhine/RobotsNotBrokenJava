package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.utils.Map2D;
import ru.enzhine.rnb.utils.TreeMap2D;
import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.World;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockFactory;
import ru.enzhine.rnb.world.block.base.BlockType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

// PIECE OF SHIT
public class DynamicVoronoiChunkGenerator implements ChunkGenerator {

    private final Map2D<WorldBiome> biomes = new TreeMap2D<>();
    private final World world;
    private final Random rand;

    private float biomeDensity;
    private BlockFactory blockFactory;

    public DynamicVoronoiChunkGenerator(float biomeDensity, World world, BlockFactory blockFactory) {
        this(biomeDensity, world, blockFactory, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    public DynamicVoronoiChunkGenerator(float biomeDensity, World world, BlockFactory blockFactory, long randSeed) {
        this.biomeDensity = biomeDensity;
        this.blockFactory = blockFactory;
        this.world = world;
        this.rand = new Random(randSeed);
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

    private long biomeSize() {
        return (long) (chunkSize() / biomeDensity);
    }

    private void fillDensity(long cX, long cY, List<WorldBiome> cache) {
        long x = cX * biomeSize();
        long y = cY * biomeSize();
        long xMin = x - biomeSize();
        long W = x + biomeSize() - xMin;
        long yMin = y - biomeSize();
        long H = y + biomeSize() - yMin;

        for (int i = 0; i < 4; i++) {
            long bX = xMin + rand.nextLong(W);
            long bY = yMin + rand.nextLong(H);
            BiomeType b = rand.nextBoolean() ? BiomeType.RANDOM : BiomeType.RANDOM1;

            WorldBiome newBiome = new WorldBiome(b, bX, bY);
            biomes.put(newBiome);
            if (cache != null) {
                cache.add(newBiome);
            }
        }
    }

    private Iterable<WorldBiome> nearbyBiomes(long x, long y) {
        long xMin = x - biomeSize();
        long xMax = x + biomeSize();
        long yMin = y - biomeSize();
        long yMax = y + biomeSize();

        return biomes.withinBounds(xMin, xMax, yMin, yMax);
    }

    private Iterable<WorldBiome> nearbyChunkBiomes(long cX, long cY) {
        return nearbyBiomes(cX * biomeSize(), cY * biomeSize());
    }

    private Double dist(long x1, long y1, long x2, long y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private WorldBiome getClosestBiome(long x, long y, Iterable<WorldBiome> cache) {
        if (cache == null) {
            cache = nearbyBiomes(x, y);
        }
        double min = Double.MAX_VALUE;
        WorldBiome res = null;
        for (WorldBiome b : cache) {
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
        List<WorldBiome> nearby = new LinkedList<>();
        nearbyChunkBiomes(c.getOffsetX(), c.getOffsetY()).forEach(nearby::add);

        if (nearby.size() / 4f < biomeDensity) {
            fillDensity(c.getOffsetX(), c.getOffsetY(), nearby);
        }
        int cSize = chunkSize();
        for (int x = 0; x < cSize; ++x) {
            for (int y = 0; y < cSize; ++y) {
                long blockX = chunkGlobalX(c, (long) x);
                long blockY = chunkGlobalY(c, (long) y);
                BiomeType biome = getClosestBiome(blockX, blockY, biomes).getBiomeType();

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
        return getClosestBiome(x, y, null).getBiomeType();
    }

    @Override
    public void setBlockFactory(BlockFactory bf) {
        blockFactory = bf;
    }

    @Override
    public BlockFactory getBlockFactory() {
        return blockFactory;
    }

    public void setBiomeDensity(float biomeDensity) {
        this.biomeDensity = biomeDensity;
    }

    public Map2D<WorldBiome> getBiomes() {
        return biomes;
    }
}
