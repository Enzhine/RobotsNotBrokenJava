package ru.enzhine.rnb.utils;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TreeMap2D<V extends Placeable2D> implements Map2D<V> {

    private final SortedMap<Long, SortedMap<Long, V>> byX = new TreeMap<>();

    public class TreeMap2DIterator implements Iterator<V> {

        private final Iterator<SortedMap<Long, V>> byXIterator = byX.values().iterator();
        private Iterator<V> byYIterator = byXIterator.hasNext() ? byXIterator.next().values().iterator() : null;

        @Override
        public boolean hasNext() {
            return byYIterator != null && byYIterator.hasNext();
        }

        @Override
        public V next() {
            V val = byYIterator.next();
            if(!byYIterator.hasNext()) {
                if(!byXIterator.hasNext()) {
                    byYIterator = null;
                } else {
                    byYIterator = byXIterator.next().values().iterator();
                }
            }
            return val;
        }
    }

    public class TreeMap2DBoundedIterator implements Iterator<V> {

        private final Long yMin;
        private final Long yMax;

        private final Iterator<SortedMap<Long, V>> byXIterator;
        private Iterator<V> byYIterator;

        public TreeMap2DBoundedIterator(Long xMin, Long xMax, Long yMin, Long yMax) {
            this.yMin = yMin;
            this.yMax = yMax;

            this.byXIterator = byX.tailMap(xMin).headMap(xMax + 1).values().iterator();
            while(byXIterator.hasNext()) {
                this.byYIterator = byXIterator.next().tailMap(yMin).headMap(yMax + 1).values().iterator();

                if(this.byYIterator.hasNext()){
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return byYIterator != null && byYIterator.hasNext();
        }

        @Override
        public V next() {
            V val = byYIterator.next();
            if(!byYIterator.hasNext()) {
                if(!byXIterator.hasNext()) {
                    byYIterator = null;
                } else {
                    while(byXIterator.hasNext()) {
                        this.byYIterator = byXIterator.next().tailMap(yMin).headMap(yMax + 1).values().iterator();

                        if(this.byYIterator.hasNext()){
                            break;
                        }
                    }
                }
            }
            return val;
        }
    }

    @Override
    public V at(Long x, Long y) {
        SortedMap<Long, V> mapY = byX.get(x);
        if(mapY == null) {
            return null;
        }
        return mapY.get(y);
    }

    @Override
    public V put(V v) {
        SortedMap<Long, V> mapY = byX.computeIfAbsent(v.getX(), k -> new TreeMap<>());
        return mapY.put(v.getY(), v);
    }

    @Override
    public Iterable<V> withinBounds(Long xMin, Long xMax, Long yMin, Long yMax) {
        return new Iterable<>() {
            @NotNull
            @Override
            public Iterator<V> iterator() {
                return new TreeMap2DBoundedIterator(xMin, xMax, yMin, yMax);
            }
        };
    }

    @NotNull
    @Override
    public Iterator<V> iterator() {
        return new TreeMap2DIterator();
    }
}
