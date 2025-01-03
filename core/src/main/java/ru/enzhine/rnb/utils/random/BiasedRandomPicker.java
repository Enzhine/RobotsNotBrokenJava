package ru.enzhine.rnb.utils.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import lombok.Data;
import ru.enzhine.rnb.utils.ImmutablePair;
import ru.enzhine.rnb.utils.MutablePair;

/**
 * Walker's algorithm implementation.
 *
 * @param <T> type which instances are to be randomly picked.
 */
public class BiasedRandomPicker<T> implements RandomPicker<T> {

    @Data
    public class Bucket {
        private final T leftValue;
        private final T rightValue;
        private final float leftValueProbability;
    }

    private final List<Bucket> buckets;

    public BiasedRandomPicker(List<ImmutablePair<Integer, T>> biasedValues, Function<Float, Boolean> isEqualToZero) {
        int n = biasedValues.size();
        int sum = biasedValues.stream().mapToInt(ImmutablePair::getLeft).sum();
        List<MutablePair<Float, T>> floatMap = biasedValues.stream().map(e -> new MutablePair<>((float) e.getLeft() / sum, e.getRight())).toList();
        this.buckets = makeBuckets(floatMap, (float) 1 / n, isEqualToZero);
    }

    public BiasedRandomPicker(List<ImmutablePair<Integer, T>> biasedValues) {
        this(biasedValues, value -> (value == 0 || Math.abs(value) < 1e-6));
    }

    private List<Bucket> makeBuckets(List<MutablePair<Float, T>> biasedValues, float targetLevel, Function<Float, Boolean> isEqualToZero) {
        List<Bucket> output = new ArrayList<>();
        List<Integer> skip = new ArrayList<>();

        for (int i = 0; i < biasedValues.size(); ++i) {
            var pair = biasedValues.get(i);
            if (skip.contains(i)) {
                continue;
            }
            if (isEqualToZero.apply(pair.getLeft() - targetLevel)) {
                // filled
                output.add(new Bucket(pair.getRight(), null, 1f));
            }else if (pair.getLeft() < targetLevel) {
                // under-filled
                for (int j = i + 1; j < biasedValues.size(); ++j) {
                    var otherPair = biasedValues.get(j);

                    if (isEqualToZero.apply(otherPair.getLeft() - targetLevel) ||
                            pair.getLeft() + otherPair.getLeft() < targetLevel) {
                        continue;
                    }

                    float delta = targetLevel - pair.getLeft();
                    otherPair.setLeft(otherPair.getLeft() - delta);
                    output.add(new Bucket(pair.getRight(), otherPair.getRight(), pair.getLeft() / targetLevel));
                    pair.setLeft(targetLevel);
                    break;
                }
            }else {
                // over-filled
                for (int j = i + 1; j < biasedValues.size(); ++j) {
                    var otherPair = biasedValues.get(j);

                    if (isEqualToZero.apply(otherPair.getLeft() - targetLevel) ||
                            otherPair.getLeft() >= targetLevel) {
                        continue;
                    }

                    float delta = targetLevel - otherPair.getLeft();
                    pair.setLeft(pair.getLeft() - delta);
                    output.add(new Bucket(otherPair.getRight(), pair.getRight(), otherPair.getLeft() / targetLevel));
                    skip.add(j);
                    otherPair.setLeft(targetLevel);
                    break;
                }
                i -= 1;
            }

        }

        if (output.size() != biasedValues.size()) {
            throw new RuntimeException("Unable to build buckets");
        }
        return output;
    }

    @Override
    public T pick(Random random) {
        var bucket = buckets.get(random.nextInt(buckets.size()));
        float probability = random.nextFloat();
        return probability <= bucket.leftValueProbability ? bucket.leftValue : bucket.rightValue;
    }
}
