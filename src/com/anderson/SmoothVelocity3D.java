package com.anderson;

import javafx.geometry.Point3D;

public class SmoothVelocity3D {

    private Smoother disturbanceX;
    private Smoother disturbanceY;
    private Smoother disturbanceZ;
    private Smoother baseX;
    private Smoother baseY;
    private Smoother baseZ;

    public SmoothVelocity3D(int minRadius, int maxRadius) {
        this.baseX = new Smoother(true, Util.next(minRadius, maxRadius), Util.next(1, 5));
        this.baseY = new Smoother(false, Util.next(minRadius, maxRadius), Util.next(1, 5));
        this.baseZ = new Smoother(false, Util.next(minRadius, maxRadius), Util.next(1, 5));
        this.disturbanceX = new Smoother(false, Util.next(-2, 2), Util.next(1, 3));
        this.disturbanceY = new Smoother(true, Util.next(-2, 2), Util.next(1, 3));
        this.disturbanceZ = new Smoother(true, Util.next(-2, 2), Util.next(1, 3));
        this.disturbanceZ.reverse();
    }

    public void next_frame() {
        this.baseX.next_frame();
        this.baseY.next_frame();
        this.baseZ.next_frame();
    }

    public void reverseX() {
        this.baseX.reverse();
        this.disturbanceX.reverse();
    }

    public void reverseY() {
        this.baseY.reverse();
        this.disturbanceY.reverse();
    }

    public void reverseZ() {
        this.baseZ.reverse();
        this.disturbanceZ.reverse();
    }

    public float getX() {
        return this.baseX.get() + this.disturbanceX.get();
    }

    public float getY() {
        return this.baseY.get() + this.disturbanceY.get();
    }

    public float getZ() {
        return this.baseZ.get() + this.disturbanceZ.get();
    }

    public Point3D move(Point3D p, float min, float max) {
        double x = p.getX() + this.getX();
        double y = p.getY() + this.getY();
        double z = p.getZ() + this.getZ();
        
        if (x < min) {
            x = min;
            this.reverseX();
        } else if (x > max) {
            x = max;
            this.reverseX();
        }

        if (y < min) {
            y = min;
            this.reverseY();
        } else if (y > max) {
            y = max;
            this.reverseY();
        }

        if (z < min) {
            z = min;
            this.reverseZ();
        } else if (z > max) {
            z = max;
            this.reverseZ();
        }

        return new Point3D(x, y, z);
    }
    
    public void setLocation(float x, float y, float z) {
        this.baseX.set_radius(x);
        this.baseY.set_radius(y);
        this.baseY.set_radius(z);
    }
}