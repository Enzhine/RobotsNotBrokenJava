package ru.enzhine.rnb.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class ImmutablePair<A, B> {
    private final A left;
    private final B right;

    private ImmutablePair(A a, B b) {
        this.left = a;
        this.right = b;
    }

    public static <A, B> ImmutablePair<A, B> of(A a, B b) {
        return new ImmutablePair<>(a, b);
    }
}
