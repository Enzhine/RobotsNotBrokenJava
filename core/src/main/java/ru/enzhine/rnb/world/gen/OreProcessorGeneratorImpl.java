package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.utils.random.BiasedRandomPicker;
import ru.enzhine.rnb.utils.random.RandomPicker;
import ru.enzhine.rnb.world.block.base.BiomeType;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.gen.ores.OreProcessor;
import ru.enzhine.rnb.world.gen.ores.SingleOreProcessor;

import java.util.HashMap;
import java.util.Random;

public class OreProcessorGeneratorImpl implements OreProcessorGenerator {

    private final OreProcessor hardStoneProcessor;

    private final RandomPicker<OreProcessor> lightAreaPicker;

    public OreProcessorGeneratorImpl() {
        hardStoneProcessor = new SingleOreProcessor(BlockType.HARD_STONE);

        var lightAreaPickerMap = new HashMap<Integer, OreProcessor>();
        lightAreaPickerMap.put(95, null);
        lightAreaPickerMap.put(5, hardStoneProcessor);

        lightAreaPicker = new BiasedRandomPicker<>(lightAreaPickerMap);
    }

    @Override
    public OreProcessor getOreProcessor(long x, long y, BiomeType biomeType, Random random) {
        switch (biomeType) {
            case LIGHT -> {
                return lightAreaPicker.pick(random);
            }
        }
        return null;
    }
}
