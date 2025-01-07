package ru.enzhine.rnb.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MathUtilsTest {

    @Test
    void __correct() {
        Assertions.assertEquals(1, MathUtils.symCeil(0.1f));
        Assertions.assertEquals(2, MathUtils.symCeil(1.1f));
        Assertions.assertEquals(0, MathUtils.symCeil(0f));
        Assertions.assertEquals(-1, MathUtils.symCeil(-0.1f));
        Assertions.assertEquals(-2, MathUtils.symCeil(-1.1f));
    }
}
