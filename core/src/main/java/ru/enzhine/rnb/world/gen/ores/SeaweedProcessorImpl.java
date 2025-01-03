package ru.enzhine.rnb.world.gen.ores;

import lombok.AllArgsConstructor;
import ru.enzhine.rnb.utils.MathUtils;
import ru.enzhine.rnb.utils.Placeable2DUtils;
import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.block.base.BlockFactory;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.gen.WorldBiome;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class SeaweedProcessorImpl implements OreProcessor {

    private final BlockFactory blockFactory;

    @Override
    public void process(long x, long y, Chunk c, List<WorldBiome> worldBiomes, Random r) {
        var biomes = Placeable2DUtils.sortBySquaredDistance(x, y, worldBiomes);
        int cX = MathUtils.remainder(x, c.getWorld().chunkSize());
        int cY = MathUtils.remainder(y, c.getWorld().chunkSize());
        Block block = c.get(cX, cY);

        if (biomes.get(0).getLeft() < 10 * 10) {
            c.set(blockFactory.makeBlock(BlockType.SEAWEED, x, y, block.getBiome(), c));
        }
    }
}
