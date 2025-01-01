package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.World;
import ru.enzhine.rnb.world.WorldBiomeFactory;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.ceil;
import static java.lang.Math.log10;

public class LazyDeterminedVoronoiChunkGenerator implements ChunkGenerator {

    private final World world;
    private BlockFactory blockFactory;
    private WorldBiomeFactory worldBiomeFactory;
    private BlockTypeGenerator blockTypeGenerator;
    private BiomeTypeGenerator biomeTypeGenerator;
    private OreProcessorGenerator oreProcessorGenerator;

    private final long seed;
    private final Random rand;
    private final float bpc;
    private final float gp;

    public LazyDeterminedVoronoiChunkGenerator(
            World world,
            BlockFactory blockFactory,
            WorldBiomeFactory worldBiomeFactory,
            BlockTypeGenerator blockTypeGenerator,
            BiomeTypeGenerator biomeTypeGenerator,
            OreProcessorGenerator oreProcessorGenerator,
            long seed,
            float biomesPerChunk,
            float gapProbability
    ) {
        this.world = world;
        this.blockFactory = blockFactory;
        this.worldBiomeFactory = worldBiomeFactory;
        this.blockTypeGenerator = blockTypeGenerator;
        this.biomeTypeGenerator = biomeTypeGenerator;
        this.oreProcessorGenerator = oreProcessorGenerator;
        this.seed = seed;

        this.rand = new Random();
        assert biomesPerChunk > 0f;
        this.bpc = biomesPerChunk;
        assert gapProbability > 0f;
        assert gapProbability <= 1f;
        this.gp = gapProbability;
    }

    public LazyDeterminedVoronoiChunkGenerator(
            World world,
            BlockFactory blockFactory,
            WorldBiomeFactory worldBiomeFactory,
            BlockTypeGenerator blockTypeGenerator,
            BiomeTypeGenerator biomeTypeGenerator,
            OreProcessorGenerator oreProcessorGenerator,
            float biomesPerChunk,
            float gapProbability
    ) {
        this(world, blockFactory, worldBiomeFactory, blockTypeGenerator, biomeTypeGenerator, oreProcessorGenerator,
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), biomesPerChunk, gapProbability);
    }

    @Override
    public void fillChunk(Chunk c) {
        var biomes = getChunkBiomes(c.getOffsetX(), c.getOffsetY());
        for (int x = 0; x < chunkSize(); ++x) {
            for (int y = 0; y < chunkSize(); ++y) {
                long blockX = chunkGlobalXY(c.getOffsetX()) + (long) x;
                long blockY = chunkGlobalXY(c.getOffsetY()) + (long) y;
                BiomeType biomeType = getClosestBiome(blockX, blockY, biomes).get();

                var oreProcessor = oreProcessorGenerator.getOreProcessor(blockX, blockY, biomeType, this.rand);
                if (oreProcessor != null) {
                    oreProcessor.process(blockX, blockY, c, biomeType, blockFactory, this.rand);
                }else {
                    var blockType = blockTypeGenerator.getBlockType(blockX, blockY, biomeType, this.rand);
                    c.set(blockFactory.makeBlock(blockType, blockX, blockY, biomeType, c));
                }
            }
        }
    }

    @Override
    public BiomeType getBiome(long x, long y) {
        var biomes = getChunkBiomes(x / chunkSize(), y / chunkSize());
        return getClosestBiome(x, y, biomes).get();
    }

    @Override
    public void setBlockFactory(BlockFactory bf) {
        blockFactory = bf;
    }

    @Override
    public BlockFactory getBlockFactory() {
        return blockFactory;
    }

    private Long chunkGlobalXY(long cXY) {
        return cXY * chunkSize();
    }

    private int chunkSize() {
        return world.chunkSize();
    }

    private double dist(long x1, long y1, long x2, long y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private long getChunkHash(long cX, long cY) {
        return seed - cX * 100003L + cY * 8497519L;
    }

    public void appendCachedChunkBiomes(long cX, long cY, List<WorldBiome> cache) {
        this.rand.setSeed(getChunkHash(cX, cY));

        float probability = this.rand.nextFloat();
        int count;
        if (this.bpc > 1.0f) {
            count = (int) ceil(probability * (this.bpc + 1) - 1);
        } else {
            count = probability < this.bpc ? 1 : 0;
        }
        for (int i = 0; i < count; i++) {
            long x = chunkGlobalXY(cX) + rand.nextInt(chunkSize());
            long y = chunkGlobalXY(cY) + rand.nextInt(chunkSize());
            var biomeType = biomeTypeGenerator.getBiomeType(x, y, this.rand);

            cache.add(worldBiomeFactory.makeWorldBiome(x, y, biomeType));
        }
    }

    private int lookupRadius() {
        if (this.bpc > 1.0f) {
            return (int) ceil(log10(this.gp) / log10(1D / (this.bpc + 1D)));
        } else {
            return (int) ceil(log10(this.gp) / log10(1 - this.bpc));
        }
    }

    private List<WorldBiome> getChunkBiomes(long cX, long cY) {
        int r = lookupRadius();
        var biomes = new LinkedList<WorldBiome>();

        for (long x = cX - r; x <= cX + r; x++) {
            for (long y = cY - r; y <= cY + r; y++) {
                appendCachedChunkBiomes(x, y, biomes);
            }
        }
        return biomes;
    }

    private WorldBiome getClosestBiome(long x, long y, List<WorldBiome> biomes) {
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
}
