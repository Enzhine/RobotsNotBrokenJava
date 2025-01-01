package ru.enzhine.rnb.world.gen.ores;

import lombok.AllArgsConstructor;
import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockFactory;
import ru.enzhine.rnb.world.block.base.BlockType;

import java.util.Random;

@AllArgsConstructor
public class SingleOreProcessor implements OreProcessor {

    private final BlockType blockType;

    @Override
    public void process(long x, long y, Chunk c, BiomeType biomeType, BlockFactory blockFactory, Random r) {
        var block = blockFactory.makeBlock(blockType, x, y, biomeType, c);
        c.set(block);
    }
}
