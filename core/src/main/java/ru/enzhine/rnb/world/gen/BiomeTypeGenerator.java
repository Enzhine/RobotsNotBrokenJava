package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.world.block.base.BiomeType;

import java.util.Random;

public interface BiomeTypeGenerator {
    BiomeType getBiomeType(Long x, Long y, Random r);
}
