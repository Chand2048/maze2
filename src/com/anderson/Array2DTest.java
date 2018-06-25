package com.anderson;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

class Array2DTest {

    @org.junit.jupiter.api.Test
    void  putGet() {
        int height = 10;
        int width = 9;
        float yMult = 1000.0f;

        Array2D a = new Array2D(height, width);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                a.set(y, x, y * yMult + x);
            }
        }

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                float expected = y * yMult + x;
                assertEquals(expected, a.get(y, x));
            }
        }
    }

    @org.junit.jupiter.api.Test
    void  convert() {
        int mapping[][] = new int[10][9];
        int height = mapping.length;
        int width = mapping[0].length;
        float yMult = 1000.0f;

        Array2D a = new Array2D(height, width);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                a.set(y, x, y * yMult + x);
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
                assertEquals(expected, a.get(y, x));
            }
        }
    }
}