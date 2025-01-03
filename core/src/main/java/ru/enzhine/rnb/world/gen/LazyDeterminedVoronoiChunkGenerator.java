package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.utils.Placeable2DUtils;
import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.World;
import ru.enzhine.rnb.world.WorldBiomeFactory;
import ru.enzhine.rnb.world.block.base.BiomeType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.ceil;
import static java.lang.Math.log10;

public class LazyDeterminedVoronoiChunkGenerator implements ChunkGenerator {

    private final World world;
    private final WorldBiomeFactory worldBiomeFactory;
    private final BlockGenerator blockTypeGenerator;
    private final BiomeTypeGenerator biomeTypeGenerator;
    private final OreProcessorGenerator oreProcessorGenerator;

    private final long seed;
    private final Random rand;
    private final float bpc;
    private final float gp;

    public LazyDeterminedVoronoiChunkGenerator(
            World world,
            WorldBiomeFactory worldBiomeFactory,
            BlockGenerator blockTypeGenerator,
            BiomeTypeGenerator biomeTypeGenerator,
            OreProcessorGenerator oreProcessorGenerator,
            long seed,
            float biomesPerChunk,
            float gapProbability
    ) {
        this.world = world;
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
            WorldBiomeFactory worldBiomeFactory,
            BlockGenerator blockTypeGenerator,
            BiomeTypeGenerator biomeTypeGenerator,
            OreProcessorGenerator oreProcessorGenerator,
            float biomesPerChunk,
            float gapProbability
    ) {
        this(world, worldBiomeFactory, blockTypeGenerator, biomeTypeGenerator, oreProcessorGenerator,
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), biomesPerChunk, gapProbability);
    }

    @Override
    public void fillChunk(Chunk c) {
        var biomes = getChunkBiomes(c.getOffsetX(), c.getOffsetY());
        for (int x = 0; x < chunkSize(); ++x) {
            for (int y = 0; y < chunkSize(); ++y) {
                long blockX = chunkGlobalXY(c.getOffsetX()) + (long) x;
                long blockY = chunkGlobalXY(c.getOffsetY()) + (long) y;

                var block = blockTypeGenerator.getBlock(blockX, blockY, c, biomes, this.rand);
                c.set(block);
                var oreProcessor = oreProcessorGenerator.getOreProcessor(blockX, blockY, biomes, this.rand);
                if (oreProcessor != null) {
                    oreProcessor.process(blockX, blockY, c, biomes, this.rand);
                }
            }
        }
    }

    @Override
    public BiomeType getBiome(long x, long y) {
        var biomes = getChunkBiomes(x / chunkSize(), y / chunkSize());
        return Placeable2DUtils.findClosest(x, y, biomes).get();
    }

    private Long chunkGlobalXY(long cXY) {
        return cXY * chunkSize();
    }

    private int chunkSize() {
        return world.chunkSize();
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
}
