package com.anderson;

import javafx.geometry.Point3D;

import java.awt.*;

public final class ColorRamp {

    private final float SCALE;
    private final Color[] colors;
    private final SmoothVelocity3D colorPicker;
    private int pivot;
    private Point3D lastPoint;

    public ColorRamp() {
        this.SCALE = Util.next(300, 1200);
        this.colors = new Color[256];
        this.colorPicker = new SmoothVelocity3D(4, 10);

        this.pivot = 0;
        this.lastPoint = new Point3D(Util.next(0f, SCALE), Util.next(0f, SCALE), Util.next(0f, SCALE));
        for (int i = 0; i < this.colors.length; ++i) {
            this.colors[i] = this.moveColor();
            this.colorPicker.next_frame();
        }
    }

    private Color moveColor() {
        this.colorPicker.next_frame();
        this.lastPoint = this.colorPicker.move(this.lastPoint, 0f, SCALE);
        return new Color(
                (float)this.lastPoint.getX() / SCALE,
                (float)this.lastPoint.getY() / SCALE,
                (float)this.lastPoint.getZ() / SCALE);
    }

    public Color get(float i) {
        int n = (int) (pivot + (i * this.colors.length));
        if (n >= this.colors.length) {
            n = n - this.colors.length;
        }

        return this.colors[n];
    }

    public void next_frame() {
        this.colors[this.pivot] = this.moveColor();

        this.pivot++;
        if (this.pivot >= this.colors.length) {
            this.pivot = 0;
        }
    }

}
