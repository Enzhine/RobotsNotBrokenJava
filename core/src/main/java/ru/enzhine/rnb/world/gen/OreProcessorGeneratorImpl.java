package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.utils.ImmutablePair;
import ru.enzhine.rnb.utils.Placeable2DUtils;
import ru.enzhine.rnb.utils.random.BiasedRandomPicker;
import ru.enzhine.rnb.utils.random.RandomPicker;
import ru.enzhine.rnb.world.block.base.BlockFactory;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.gen.ores.*;

import java.util.List;
import java.util.Random;

public class OreProcessorGeneratorImpl implements OreProcessorGenerator {

    private final BlockFactory blockFactory;

    private final RandomPicker<OreProcessor> lightAreaPicker;
    private final RandomPicker<OreProcessor> desertAreaPicker;
    private final RandomPicker<OreProcessor> organicAreaPicker;
    private final RandomPicker<OreProcessor> uniqueAreaPicker;

    public OreProcessorGeneratorImpl(BlockFactory blockFactory) {
        this.blockFactory = blockFactory;

        lightAreaPicker = new BiasedRandomPicker<>(List.of(
                ImmutablePair.of(88, null),
                ImmutablePair.of(5, new CoalOreProcessorImpl(this.blockFactory)),
                ImmutablePair.of(5, new CopperOreProcessorImpl(this.blockFactory)),
                ImmutablePair.of(2, new SingleOreProcessor(this.blockFactory, BlockType.OXYLITTE))
        ));

        desertAreaPicker = new BiasedRandomPicker<>(List.of(
                ImmutablePair.of(95, null),
                ImmutablePair.of(2, new CoalOreProcessorImpl(this.blockFactory)),
                ImmutablePair.of(2, new CopperOreProcessorImpl(this.blockFactory)),
                ImmutablePair.of(1, new SingleOreProcessor(this.blockFactory, BlockType.OXYLITTE))
        ));

        organicAreaPicker = new BiasedRandomPicker<>(List.of(
                ImmutablePair.of(25, null),
                ImmutablePair.of(65, new SeaweedProcessorImpl(this.blockFactory)),
                ImmutablePair.of(5, new SingleOreProcessor(this.blockFactory, BlockType.OXYLITTE)),
                ImmutablePair.of(5, new SingleOreProcessor(this.blockFactory, BlockType.RICH_SOIL))
        ));

        uniqueAreaPicker = new BiasedRandomPicker<>(List.of(
                ImmutablePair.of(68, null),
                ImmutablePair.of(10, new CoalOreProcessorImpl(this.blockFactory)),
                ImmutablePair.of(10, new CopperOreProcessorImpl(this.blockFactory)),
                ImmutablePair.of(5, new SingleOreProcessor(this.blockFactory, BlockType.COAL_CLUSTER)),
                ImmutablePair.of(5, new SingleOreProcessor(this.blockFactory, BlockType.COPPER_CLUSTER)),
                ImmutablePair.of(2, new SingleOreProcessor(this.blockFactory, BlockType.OXYLITTE))
        ));
    }

    @Override
    public OreProcessor getOreProcessor(long x, long y, List<WorldBiome> worldBiomes, Random random) {
        var closesBiome = Placeable2DUtils.findClosest(x, y, worldBiomes).get();
        switch (closesBiome) {
            case LIGHT -> {
                return lightAreaPicker.pick(random);
            }
            case ORGANIC -> {
                return organicAreaPicker.pick(random);
            }
            case DESERT -> {
                return desertAreaPicker.pick(random);
            }
            case UNIQUE -> {
                return uniqueAreaPicker.pick(random);
            }
        }
        return null;
    }
}
