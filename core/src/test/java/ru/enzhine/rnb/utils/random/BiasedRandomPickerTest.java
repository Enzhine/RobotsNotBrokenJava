package ru.enzhine.rnb.utils.random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

public class BiasedRandomPickerTest {

    private Random r = new Random(32);

    @Test
    public void pick_manyAttempts_closeToProbability() {
        int n = 100_000;
        var picker = new BiasedRandomPicker<>(Map.of(
                5, 0,
                15, 1,
                50, 2,
                30, 3
        ));

        int count0 = 0;
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        for (int i = 0; i < n; i++) {
            var value = picker.pick(r);

            switch (value){
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
        Assertions.assertEquals(proba0, 0.05f);
        Assertions.assertEquals(proba1, 0.15f);
        Assertions.assertEquals(proba2, 0.50f);
        Assertions.assertEquals(proba3, 0.30f);
    }
}
