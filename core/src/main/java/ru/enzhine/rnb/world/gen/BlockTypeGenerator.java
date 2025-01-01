package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;

import java.util.Random;

public interface BlockTypeGenerator {
    BlockType getBlockType(Long x, Long y, BiomeType biomeType, Random r);
}
