package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.utils.MathUtils;
import ru.enzhine.rnb.world.block.base.BiomeType;

import java.util.Random;

public class BiomeGeneratorImpl implements BiomeTypeGenerator {
    @Override
    public BiomeType getBiomeType(Long x, Long y, Random r) {
        var dist = MathUtils.radiusVec(x, y);
        if (dist < 50d) {
            return switch (r.nextInt(2)) {
                case 0 -> BiomeType.LIGHT;
                case 1 -> BiomeType.DESERT;
                default -> throw new RuntimeException("Unexpected value");
            };
        } else if (dist < 100d) {
            return switch (r.nextInt(3)) {
                case 0 -> BiomeType.DESERT;
                case 1 -> BiomeType.ORGANIC;
                case 2 -> BiomeType.UNIQUE;
                default -> throw new RuntimeException("Unexpected value");
            };
        }
        return BiomeType.HEAVY;
    }
}
