package ru.enzhine.rnb.utils.random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.enzhine.rnb.utils.ImmutablePair;

import java.util.List;
import java.util.Random;

public class BiasedRandomPickerTest {

    private Random r = new Random(32);

    @Test
    public void pick_manyAttempts_closeToProbability() {
        int n = 100_000;
        var picker = new BiasedRandomPicker<>(List.of(
                ImmutablePair.of(5, 0),
                ImmutablePair.of(15, 1),
                ImmutablePair.of(50, 2),
                ImmutablePair.of(30, 3)
        ));

        int count0 = 0;
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        for (int i = 0; i < n; i++) {
            var value = picker.pick(r);

            switch (value) {
                case 0 -> ++count0;
                case 1 -> ++count1;
                case 2 -> ++count2;
                case 3 -> ++count3;
                default -> throw new RuntimeException("Unexpected switch branch");
            }
        }
        float proba0 = Math.round((float) count0 / n * 100) / 100f;
        float proba1 = Math.round((float) count1 / n * 100) / 100f;
        float proba2 = Math.round((float) count2 / n * 100) / 100f;
        float proba3 = Math.round((float) count3 / n * 100) / 100f;
        Assertions.assertEquals(0.05f, proba0);
        Assertions.assertEquals(0.15f, proba1);
        Assertions.assertEquals(0.5f, proba2);
        Assertions.assertEquals(0.3f, proba3);
    }
}
