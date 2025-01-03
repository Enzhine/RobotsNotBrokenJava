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

    public static long squaredDistanceBetween(long x1, long y1, long x2, long y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    public static double distanceBetween(long x1, long y1, long x2, long y2) {
        return Math.sqrt(squaredDistanceBetween(x1, y1, x2, y2));
    }

    public static float lerp(float from, float to, float progress) {
        return from + (to - from) * progress;
    }
}
