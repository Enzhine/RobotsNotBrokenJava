package ru.enzhine.rnb.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Objects;

public class TreeMap2DTest {

    public static class Dummy2D implements Placeable2D {

        private final long x;
        private final long y;

        public Dummy2D(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public Long getX() {
            return x;
        }

        @Override
        public Long getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Dummy2D dummy2D = (Dummy2D) o;
            return x == dummy2D.x && y == dummy2D.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return String.format("Dummy of %d %d", x, y);
        }
    }

    @Test
    public void validation__success() {
        Map2D<Dummy2D> map = new TreeMap2D<>();

        map.put(new Dummy2D(1L,1L));
        map.put(new Dummy2D(-1L,1L));
        map.put(new Dummy2D(-1L,-1L));
        map.put(new Dummy2D(1L,-1L));
        map.put(new Dummy2D(2L,2L));
        map.put(new Dummy2D(-2L,2L));
        map.put(new Dummy2D(-2L,-2L));
        map.put(new Dummy2D(2L,-2L));

        var actual = new LinkedHashSet<Dummy2D>();
        map.withinBounds(-1L, 1L, -1L, 1L).forEach(actual::add);

        var expected = new LinkedHashSet<Dummy2D>();
        expected.add(new Dummy2D(1L,1L));
        expected.add(new Dummy2D(-1L,1L));
        expected.add(new Dummy2D(-1L,-1L));
        expected.add(new Dummy2D(1L,-1L));

        System.out.println("Actual:");
        actual.forEach(System.out::println);
        System.out.println("Expected:");
        expected.forEach(System.out::println);

        Assertions.assertEquals(expected, actual);
    }
}
