package ru.enzhine.rnb.utils;

public class MathUtils {
    public static int remainder(long value, int mod) {
        int res = (int) (value % mod);
        return res >= 0 ? res : res + mod;
    }

    public static int remainder(int value, int mod) {
        int res = value % mod;
        return res >= 0 ? res : res + mod;
    }

    public static double radiusVec(long x, long y) {
        return Math.sqrt(x * x + y * y);
    }
}
