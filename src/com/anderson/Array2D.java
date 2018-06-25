package com.anderson;

import java.awt.geom.Point2D;

public class Array2D {
    private static boolean DEBUG = false;
    private final int height;
    private final int width;
    private final float minValue;
    private final float maxValue;
    private float a[][];
    
    public Array2D(int width, int height) {
        this.a = new float[height][width];
        this.height = height;
        this.width = width;
        this.minValue = Float.MIN_VALUE;
        this.maxValue = Float.MAX_VALUE;
        this.reset(Float.MIN_VALUE);
    }

    public Array2D(int width, int height, float minValue, float maxValue) {
        this.a = new float[height][width];
        this.height = height;
        this.width = width;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.reset(minValue);
    }

    public static Array2D blend_average(Array2D a1, Array2D a2) {
        Array2D out = new Array2D(a1.height, a1.width);
        for (int y = 0; y < a1.height; ++y) {
            for (int x = 0; x < a1.width; ++x) {
                out.set(x, y, (a1.get(x,y) + a2.get(x,y))/ 2f);
            }
        }

        return out;
    }

    public void reset(float val) {
        for (int y = 0; y < this.height; ++y) {
            for (int x = 0; x < this.width; ++x) {
                this.a[y][x] = val;
            }
        }
    }

    public float get(int x, int y) {
        if (DEBUG)
            System.out.format("a[%d][%d]->%f\n", y,x, a[y][x]);
        return a[y][x];
    }

    public float get(Point2D p) {
        return this.get((int)p.getX(), (int)p.getY());
    }

    public int convert(Point2D p) {
        return (int)(p.getY() * this.width + p.getX());
    }

    public Point2D convert(int i) {
        return new Point2D.Float(i % this.width, i / width);
    }

    public float get(int i) {
        return this.get(i % this.width, i / width);
    }

    public boolean in_bounds(Point2D p) {
        return p.getX() >= 0 && p.getX() < this.width && p.getY() >= 0 && p.getY() < this.height;
    }

    public float get_up(Point2D p, float invalid) {
        if (!this.in_bounds(p)) return invalid;
        return get((int)p.getX() - 1, (int)p.getY());
    }

    public float get_down(Point2D p, float invalid) {
        if (!this.in_bounds(p)) return invalid;
        return get((int)p.getX() + 1, (int)p.getY());
    }

    public float get_right(Point2D p, float invalid) {
        if (!this.in_bounds(p)) return invalid;
        return get((int)p.getX(), (int)p.getY() + 1);
    }

    public float get_left(Point2D p, float invalid) {
        if (!this.in_bounds(p)) return invalid;
        return get((int)p.getX(), (int)p.getY() - 1);
    }

    public float get_wrap(int x, int y) {
        y = wrap(y, height);
        x = wrap(x, width);
        return get(x, y);
    }

    public void set(int i, float val) {
        this.set(i / this.width, i % width, val);
    }

    public void set(Point2D p, float val) {
        this.set((int)p.getX(), (int)p.getY(), val);
    }

    public void set(int x, int y, float val) {
        if (DEBUG)
            System.out.format("a[%d][%d]<-%f\n", y, x, val);
        if (this.maxValue != Float.MAX_VALUE && val > this.maxValue) {
            val = this.maxValue;
        }
        if (this.minValue != Float.MIN_VALUE && val < this.minValue) {
            val = this.minValue;
        }

        a[y][x] = val;
    }

    public void add(int x, int y, float val) {
        if (DEBUG)
            System.out.format("a[%d][%d]<-(%f + %f)\n", y, x, a[y][x], val);

        val = a[y][x] + val;
        if (this.maxValue != Float.MAX_VALUE && val > this.maxValue) {
            val = this.maxValue;
        }
        if (this.minValue != Float.MIN_VALUE && val < this.minValue) {
            val = this.minValue;
        }

        a[y][x] += val;
    }

    private static int wrap(int val, int max) {
        if (val < 0)
            return val * -1;
        else if (val > max - 1)
            return val - max + 1;
        
        return val;
    }

    public int minX() {
        return 0;
    }

    public int minY() {
        return 0;
    }

    public int maxX() {
        return this.width -1;
    }

    public int maxY() {
        return this.height -1;
    }

    public int height() {
        return this.height;
    } 

    public int width() {
        return this.width;
    }
}   