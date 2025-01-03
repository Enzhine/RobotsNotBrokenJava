package ru.enzhine.rnb.world.gen.ores;

import lombok.AllArgsConstructor;
import ru.enzhine.rnb.utils.Placeable2DUtils;
import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.block.base.BlockFactory;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.gen.WorldBiome;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class SingleOreProcessor implements OreProcessor {

    private final BlockFactory blockFactory;
    private final BlockType blockType;

    @Override
    public void process(long x, long y, Chunk c, List<WorldBiome> worldBiomes, Random r) {
        var closesBiome = Placeable2DUtils.findClosest(x, y, worldBiomes).get();
        var block = this.blockFactory.makeBlock(blockType, x, y, closesBiome, c);
        c.set(block);
    }
}
