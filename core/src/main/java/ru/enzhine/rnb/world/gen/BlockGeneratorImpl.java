package ru.enzhine.rnb.world.gen;

import ru.enzhine.rnb.utils.ImmutablePair;
import ru.enzhine.rnb.utils.Placeable2DUtils;
import ru.enzhine.rnb.utils.random.StatelessRandomPicker;
import ru.enzhine.rnb.world.Chunk;
import ru.enzhine.rnb.world.block.base.Block;
import ru.enzhine.rnb.world.block.base.BlockFactory;
import ru.enzhine.rnb.world.block.base.BlockType;
import ru.enzhine.rnb.world.block.base.BiomeType;

import java.util.List;
import java.util.Random;

public class BlockGeneratorImpl implements BlockGenerator {

    private final BlockFactory blockFactory;

    public BlockGeneratorImpl(BlockFactory blockFactory) {
        this.blockFactory = blockFactory;
    }

    @Override
    public Block getBlock(long x, long y, Chunk c, List<WorldBiome> worldBiomes, Random r) {
        var biomes = Placeable2DUtils.sortBySquaredDistance(x, y, worldBiomes);
        BiomeType closestBiome = biomes.get(0).getRight().get();

        return switch (closestBiome) {
            case LIGHT -> onLightBiome(x, y, c, biomes, r);
            case ORGANIC -> onOrganicBiome(x, y, c, biomes, r);
            case UNIQUE -> onUniqueBiome(x, y, c, biomes, r);
            case DESERT -> onDesertBiome(x, y, c, biomes, r);
            case HEAVY -> onHeavyBiome(x, y, c, biomes, r);
        };
    }

    private Block onLightBiome(long x, long y, Chunk c, List<ImmutablePair<Long, WorldBiome>> worldBiomes, Random r) {
        var currentBiome = worldBiomes.get(0).getRight().get();
        BlockType blockType = null;
        if (worldBiomes.size() > 1) {
            float tillNextSquared = (worldBiomes.get(1).getLeft() - worldBiomes.get(0).getLeft()) / 2f;
            if (tillNextSquared <= 5f * 5f) {
                var nextBiome = worldBiomes.get(1).getRight().get();

                blockType = switch (nextBiome) {
                    case ORGANIC -> StatelessRandomPicker.pickOr(r, BlockType.DRY_SEAWEED, 0.1f, BlockType.SOIL);
                    case DESERT -> StatelessRandomPicker.pickOr(r, BlockType.SAND, 0.3f, BlockType.SOFT_STONE);
                    case HEAVY -> BlockType.STONE;
                    default -> null;
                };
            }
        }
        if (blockType == null) {
            blockType = StatelessRandomPicker.pickBiased(r, BlockType.SOFT_STONE, 0.7f, BlockType.STONE, 0.25f, BlockType.SOIL, 0.05f);
        }

        return blockFactory.makeBlock(blockType, x, y, currentBiome, c);
    }

    private Block onOrganicBiome(long x, long y, Chunk c, List<ImmutablePair<Long, WorldBiome>> worldBiomes, Random r) {
        var currentBiome = worldBiomes.get(0).getRight().get();
        BlockType blockType = null;
        if (worldBiomes.size() > 1) {
            float tillNextSquared = (worldBiomes.get(1).getLeft() - worldBiomes.get(0).getLeft()) / 2f;
            var nextBiome = worldBiomes.get(1).getRight().get();

            if (tillNextSquared <= 6f * 6f && nextBiome == BiomeType.DESERT) {
                blockType = StatelessRandomPicker.pickOr(r, BlockType.DRY_SEAWEED, 0.35f, BlockType.SOIL);
            } else if (tillNextSquared <= 5f * 5f) {
                blockType = switch (nextBiome) {
                    case LIGHT -> StatelessRandomPicker.pickOr(r, BlockType.SOFT_STONE, 0.5f, BlockType.SOIL);
                    case HEAVY -> StatelessRandomPicker.pickOr(r, BlockType.DRY_SEAWEED, 0.35f, BlockType.SOFT_STONE);
                    default -> null;
                };
            }
        }
        if (blockType == null) {
            blockType = StatelessRandomPicker.pickBiased(r, BlockType.DRY_SEAWEED, 0.15f, BlockType.SOIL, 0.67f, BlockType.SAND, 0.18f);
        }

        return blockFactory.makeBlock(blockType, x, y, currentBiome, c);
    }

    private Block onUniqueBiome(long x, long y, Chunk c, List<ImmutablePair<Long, WorldBiome>> worldBiomes, Random r) {
        var currentBiome = worldBiomes.get(0).getRight().get();

        return blockFactory.makeBlock(BlockType.STONE, x, y, currentBiome, c);
    }

    private Block onDesertBiome(long x, long y, Chunk c, List<ImmutablePair<Long, WorldBiome>> worldBiomes, Random r) {
        var currentBiome = worldBiomes.get(0).getRight().get();
        BlockType blockType = StatelessRandomPicker.pickBiased(r,
                ImmutablePair.of(BlockType.STONE, 0.83f),
                ImmutablePair.of(BlockType.SOFT_STONE, 0.05f),
                ImmutablePair.of(BlockType.SOIL, 0.05f),
                ImmutablePair.of(BlockType.SAND, 0.05f),
                ImmutablePair.of(BlockType.DRY_SEAWEED, 0.02f)
        );

        return blockFactory.makeBlock(blockType, x, y, currentBiome, c);
    }

    private Block onHeavyBiome(long x, long y, Chunk c, List<ImmutablePair<Long, WorldBiome>> worldBiomes, Random r) {
        var currentBiome = worldBiomes.get(0).getRight().get();
        BlockType blockType = StatelessRandomPicker.pickBiased(r, BlockType.HARD_STONE, 0.7f, BlockType.STONE, 0.2f, BlockType.SOFT_STONE, 0.1f);
        return blockFactory.makeBlock(blockType, x, y, currentBiome, c);
    }
}
