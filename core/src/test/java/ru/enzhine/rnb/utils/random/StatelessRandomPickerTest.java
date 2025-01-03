package ru.enzhine.rnb.utils.random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.enzhine.rnb.utils.ImmutablePair;

import java.util.Random;

public class StatelessRandomPickerTest {

    private Random r = new Random();

    @BeforeEach
    public void beforeEach() {
        r.setSeed(32);
    }

    @Test
    public void tryPick_manyAttempts_closeToProbability() {
        int n = 100_000;

        int count0 = 0;
        int countNull = 0;
        for (int i = 0; i < n; i++) {
            var value = StatelessRandomPicker.tryPick(r, 0, 0.23f);

            if (value == null) {
                ++countNull;
            } else if (value == 0) {
                ++count0;
            }
        }
        float proba0 = Math.round((float) count0 / n * 100) / 100f;
        float probaNull = Math.round((float) countNull / n * 100) / 100f;
        Assertions.assertEquals(0.23f, proba0);
        Assertions.assertEquals(0.77f, probaNull);
    }

    @Test
    public void pickOr_manyAttempts_closeToProbability() {
        int n = 100_000;

        int count0 = 0;
        int count1 = 0;
        for (int i = 0; i < n; i++) {
            var value = StatelessRandomPicker.pickOr(r, 0, 0.44f, 1);

            if (value == 0) {
                ++count0;
            } else if (value == 1) {
                ++count1;
            }
        }
        float proba0 = Math.round((float) count0 / n * 100) / 100f;
        float proba1 = Math.round((float) count1 / n * 100) / 100f;
        Assertions.assertEquals(0.44f, proba0);
        Assertions.assertEquals(0.56f, proba1);
    }

    @Test
    public void pickBiased_manyAttempts_closeToProbability() {
        int n = 100_000;

        int count0 = 0;
        int count1 = 0;
        int count2 = 0;
        for (int i = 0; i < n; i++) {
            var value = StatelessRandomPicker.pickBiased(r, 0, 0.05f, 1, 0.15f, 2, 0.8f);

            if (value == 0) {
                ++count0;
            } else if (value == 1) {
                ++count1;
            } else if (value == 2) {
                ++count2;
            }
        }
        float proba0 = Math.round((float) count0 / n * 100) / 100f;
        float proba1 = Math.round((float) count1 / n * 100) / 100f;
        float proba2 = Math.round((float) count2 / n * 100) / 100f;
        Assertions.assertEquals(0.05f, proba0);
        Assertions.assertEquals(0.15f, proba1);
        Assertions.assertEquals(0.8f, proba2);
    }

    @Test
    public void pickBiasedVarargs_manyAttempts_closeToProbability() {
        int n = 100_000;

        int count0 = 0;
        int count1 = 0;
        int count2 = 0;
        for (int i = 0; i < n; i++) {
            var value = StatelessRandomPicker.pickBiased(
                    r,
                    ImmutablePair.of(0, 0.05f),
                    ImmutablePair.of(1, 0.15f),
                    ImmutablePair.of(2, 0.8f)
            );

            if (value == 0) {
                ++count0;
            } else if (value == 1) {
                ++count1;
            } else if (value == 2) {
                ++count2;
            }
        }
        float proba0 = Math.round((float) count0 / n * 100) / 100f;
        float proba1 = Math.round((float) count1 / n * 100) / 100f;
        float proba2 = Math.round((float) count2 / n * 100) / 100f;
        Assertions.assertEquals(0.05f, proba0);
        Assertions.assertEquals(0.15f, proba1);
        Assertions.assertEquals(0.8f, proba2);
    }
}
