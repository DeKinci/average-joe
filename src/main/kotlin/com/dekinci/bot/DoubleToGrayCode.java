package com.dekinci.bot;

public class DoubleToGrayCode {
    private static final int minValue = -10;
    private static final int maxValue = 10;
    private static final int size = maxValue - minValue;
    private static final int bits = 16;
    private static final int pieces = 1 << bits;

    public static int doubleToGC(double d) {
        if (d > maxValue)
            d = maxValue;
        if (d < minValue)
            d = minValue;

        int res = (int) (d * pieces / size);
        return (res ^ (res >> 1));
    }

    public static double gCToDouble(int code) {
        short bin = 0;

        while (code != 0) {
            bin ^= code;
            code >>>= 1;
        }

        return (double) bin * size / pieces;
    }
}

