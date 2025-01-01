package ru.enzhine.rnb.utils.adt;

public interface Map2D<V extends Placeable2D> extends Iterable<V> {
    V at(Long x, Long y);

    V put(V v);

    Iterable<V> withinBounds(Long xMin, Long xMax, Long yMin, Long yMax);
}
