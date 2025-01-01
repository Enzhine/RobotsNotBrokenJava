package ru.enzhine.rnb.utils.random;

import java.util.Random;

public interface RandomPicker<T> {
    T pick(Random random);
}
