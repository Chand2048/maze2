package com.anderson;

import java.util.Random;

public class Util {
    public static Random r = new Random(System.nanoTime());

    public static double next(double min, double max) {
        double d = (Util.r.nextDouble() * (max - min)) + min;
        if (d > max) {
            return Math.nextDown(max);
        } else if (d < min) {
            return Math.nextUp(min);
        }

        return d;
    }

    public static float next(float min, float max) {
        double d = Util.next((double)min, (double)max);
        return (float)d;
    }

    public static double next() {
        return Util.r.nextDouble();
    }

    public static int next(int min, int max) {
        return (int)Util.next((double)min, (double)max);
    }

    public static float pivot(float mid, float diameter) {
        double p = Util.r.nextDouble() - 0.5;
        return (float)(mid + (p * diameter));
    }

}
