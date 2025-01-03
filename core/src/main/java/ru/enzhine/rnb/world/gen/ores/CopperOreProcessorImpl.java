package ru.enzhine.rnb.world.gen.ores;

import lombok.AllArgsConstructor;
import ru.enzhine.rnb.utils.MathUtils;
import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.block.base.BlockFactory;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.gen.WorldBiome;

import java.util.List;

import java.util.Random;

@AllArgsConstructor
public class CopperOreProcessorImpl implements OreProcessor {

    private final BlockFactory blockFactory;

    @Override
    public void process(long x, long y, Chunk c, List<WorldBiome> worldBiomes, Random r) {
        int cX = MathUtils.remainder(x, c.getWorld().chunkSize());
        int cY = MathUtils.remainder(y, c.getWorld().chunkSize());
        Block block = c.get(cX, cY);

        BlockType blockType = switch (block.getType()) {
            case SOFT_STONE -> BlockType.SOFT_STONE_COPPER_ORE;
            case STONE -> BlockType.STONE_COPPER_ORE;
            default -> null;
        };
        if (blockType != null) {
            c.set(blockFactory.makeBlock(blockType, x, y, block.getBiome(), c));
        }
    }
}
