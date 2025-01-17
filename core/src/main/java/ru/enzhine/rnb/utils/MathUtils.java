package ru.enzhine.rnb.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

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

    private static float easeOutCubic(float x) {
        return 1 - (1 - x) * (1 - x) * (1 - x);
    }

    public static float lerp(float from, float to, float progress) {
        return from + (to - from) * progress;
    }

    public static Color lerp(Color from, Color to, float progress) {
        return new Color(lerp(from.r, to.r, progress), lerp(from.g, to.g, progress), lerp(from.b, to.b, progress), lerp(from.a, to.a, progress));
    }

    public static float bilerp(float from, float to, float progress) {
        if (progress < 0.5f) {
            return lerp(from, to, progress * 2f);
        }
        return lerp(to, from, (progress - 0.5f) * 2f);
    }

    public static Color bilerp(Color from, Color to, float progress) {
        return new Color(bilerp(from.r, to.r, progress), bilerp(from.g, to.g, progress), bilerp(from.b, to.b, progress), bilerp(from.a, to.a, progress));
    }

    public static float cerp(float from, float to, float progress) {
        return from + (to - from) * easeOutCubic(progress);
    }

    public static Vector3 cerp(Vector3 from, Vector3 to, float progress) {
        return new Vector3(cerp(from.x, to.x, progress), cerp(from.y, to.y, progress), cerp(from.z, to.z, progress));
    }

    public static long blockPos(float xy) {
        if (xy > 0) {
            return (long) xy;
        } else {
            return (long) Math.floor(xy);
        }
    }

    public static long blockPos(double xy) {
        if (xy > 0) {
            return (long) xy;
        } else {
            return (long) Math.floor(xy);
        }
    }
}
