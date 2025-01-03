package ru.enzhine.rnb.utils.random;

import ru.enzhine.rnb.utils.ImmutablePair;

import java.util.Arrays;
import java.util.Random;

public class StatelessRandomPicker {

    public static <T> T tryPick(Random r, T t1, float p1) {
        return r.nextFloat() <= p1 ? t1 : null;
    }

    public static <T> T pickOr(Random r, T t1, float p1, T else_) {
        return r.nextFloat() <= p1 ? t1 : else_;
    }

    public static <T> T pickBiased(Random r, T t1, float p1, T t2, float p2) {
        float total = p1 + p2;
        p1 /= total;

        return r.nextFloat() <= p1 ? t1 : t2;
    }

    public static <T> T pickBiased(Random r, T t1, float p1, T t2, float p2, T t3, float p3) {
        float total = p1 + p2 + p3;
        float rand = r.nextFloat();
        p1 /= total;
        p2 /= total;

        if (rand <= p1) {
            return t1;
        } else if (rand - p1 <= p2) {
            return t2;
        }
        return t3;
    }

    public static <T> T pickBiased(Random r, T t1, float p1, T t2, float p2, T t3, float p3, T t4, float p4) {
        float total = p1 + p2 + p3 + p4;
        float rand = r.nextFloat();
        p1 /= total;
        p2 /= total;
        p3 /= total;

        if (rand <= p1) {
            return t1;
        } else if (rand - p1 <= p2) {
            return t2;
        } else if (rand - p1 - p2 <= p3) {
            return t3;
        }
        return t4;
    }

    @SafeVarargs
    public static <T> T pickBiased(Random r, ImmutablePair<T, Float>... tps) {
        if (tps.length == 0) {
            throw new RuntimeException("No values and probabilities provided");
        }
        float total = Arrays.stream(tps).map(ImmutablePair::getRight).reduce(0f, Float::sum);
        float rand = r.nextFloat();
        for (ImmutablePair<T, Float> tp : tps) {
            if (rand <= tp.getRight() / total) {
                return tp.getLeft();
            }
            rand -= tp.getRight() / total;
        }
        return tps[tps.length - 1].getLeft();
    }
}
