package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;

import java.util.Random;

public class BlockTypeGeneratorImpl implements BlockTypeGenerator {
    @Override
    public BlockType getBlockType(Long x, Long y, BiomeType biomeType, Random r) {
        switch (biomeType) {
            case LIGHT, ORGANIC, UNIQUE -> {
                return switch (r.nextInt(2)) {
                    case 0 -> BlockType.DIRT;
                    case 1 -> BlockType.SOFT_STONE;
                    default -> throw new RuntimeException("Unexpected value");
                };
            }
            case DESERT -> {
                return BlockType.SAND;
            }
            case HEAVY -> {
                return switch (r.nextInt(2)) {
                    case 0 -> BlockType.STONE;
                    case 1 -> BlockType.HARD_STONE;
                    default -> throw new RuntimeException("Unexpected value");
                };
            }
        }
        throw new RuntimeException("Unexpected branch");
    }
}
