package com.anderson;

import java.awt.geom.Point2D;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Array2DTest {

    @Test
    void putGetXY() {
        int height = 10;
        int width = 9;
        float yMult = 1000.0f;

        Array2D a = new Array2D(width, height);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                a.set(x, y, y * yMult + x);
            }
        }

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                float expected = y * yMult + x;
                assertEquals(expected, a.get(x, y));
            }
        }
    }

    @Test
    void putGetPoint2D() {
        int height = 10;
        int width = 9;
        float yMult = 1000.0f;

        Array2D a = new Array2D(width, height);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Point2D p = new Point2D.Float(x, y);
                a.set(p, y * yMult + x);
            }
        }

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                float expected = y * yMult + x;
                Point2D p = new Point2D.Float(x, y);
                assertEquals(expected, a.get(p));
            }
        }
    }

    @Test
    void convert() {
        int mapping[][] = new int[10][9];
        int height = mapping.length;
        int width = mapping[0].length;
        float yMult = 1000.0f;

        Array2D a = new Array2D(width, height);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                a.set(x, y, y * yMult + x);
                mapping[y][x] = a.convert(new Point2D.Float(x, y));
            }
        }

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int i = mapping[y][x];
                Point2D p = new Point2D.Float(x, y);
                assertEquals(i, a.convert(p));
                assertEquals(p, a.convert(i));

                float expected = y * yMult + x;
                assertEquals(expected, a.get(i));
                assertEquals(expected, a.get(x, y));
            }
        }
    }

    private Array2D fillArray2D(float start, float end, float inc) {
        // Create with only 0.0 -> 0.5
        Array2D a = new Array2D(5, 10, 0.0f, 1.0f);
        float val = start;
        for (int y = 0; y < a.height(); ++y) {
            for (int x = 0; x < a.width(); ++x) {
                a.set(x, y, val);
                val += inc;
            }
        }

        a.set(a.maxX(), a.maxY(), end);
        return a;
    }

    @Test
    void maximizeScaleZeroBased() {
        // Create with only 0.0 -> 0.5
        Array2D a = fillArray2D(0.0f, 0.5f, 0.01f);

        float[] minMax = a.findMinMax();
        assertEquals(0f, minMax[0]);
        assertEquals(0.5f, minMax[1]);

        a.maximizeRange();
        minMax = a.findMinMax();
        assertEquals(0f, minMax[0]);
        assertEquals(1f, minMax[1]);
    }

    @Test
    void maximizeScaleOffset() {
        // Create with only 0.25 -> 0.75
        Array2D a = fillArray2D(0.25f, 0.75f, 0.01f);

        float[] minMax = a.findMinMax();
        assertEquals(0.25f, minMax[0]);
        assertEquals(0.75f, minMax[1]);

        a.maximizeRange();
        minMax = a.findMinMax();
        assertEquals(0f, minMax[0]);
        assertEquals(1f, minMax[1]);
    }

    @Test
    void blend() {
        Array2D a = fillArray2D(0.25f, 0.25f, 0.00f);
        Array2D b = fillArray2D(0.75f, 0.75f, 0.00f);
        Array2D c = Array2D.blend_average(a, b);

        assertEquals(a.minValue, c.minValue);
        assertEquals(a.maxValue, c.maxValue);
        assertEquals(a.width(), c.width());
        assertEquals(a.height(), c.height());

        float[] minMax = c.findMinMax();
        assertEquals(0.5f, minMax[0]);
        assertEquals(0.5f, minMax[1]);
    }
}