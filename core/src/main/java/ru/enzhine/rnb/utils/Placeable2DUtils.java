package ru.enzhine.rnb.utils;

import ru.enzhine.rnb.utils.adt.Placeable2D;
import ru.enzhine.rnb.world.gen.WorldBiome;

import java.util.*;

public class Placeable2DUtils {

    public static <T extends Placeable2D> List<ImmutablePair<Long, T>> sortBySquaredDistance(long x, long y, Collection<T> placeable2Ds) {
        List<ImmutablePair<Long, T>> out = new ArrayList<>();
        placeable2Ds.forEach(p2d -> out.add(ImmutablePair.of(MathUtils.squaredDistanceBetween(x, y, p2d.getX(), p2d.getY()), p2d)));

        Collections.sort(out, Comparator.comparing(ImmutablePair::getLeft));
        return out;
    }

    public static <T extends Placeable2D> T findClosest(long x, long y, Collection<T> placeable2Ds) {
        long min = Long.MAX_VALUE;
        T out = null;

        for (T t : placeable2Ds) {
            long dist = MathUtils.squaredDistanceBetween(x, y, t.getX(), t.getY());
            if (dist < min) {
                min = dist;
                out = t;
            }
        }
        return out;
    }
}
