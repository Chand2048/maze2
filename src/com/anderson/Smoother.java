package com.anderson;

public class Smoother {
    private final double deg_to_rad = 57.29576;

    private double radius;
    private double degree;
    private double speed;
    private boolean isSin;
    private double direction;

    public Smoother(boolean isSin, double radius, double speed) {
        this.isSin = isSin;
        this.degree = 0;
        this.radius = radius;
        this.speed = speed;
        this.direction = 1.0;
    }

    public void reverse() {
        this.direction = this.direction * -1;
    }

    public void set_radius(double radius) {
        this.radius = radius;
    }

    public void next_frame() {
        this.degree += this.speed;
        if (this.degree > 360.0) {
            this.degree = this.degree % 360.0;
        }
    }

    public double get() {
        if (this.isSin) {
            return Math.sin(this.degree / this.deg_to_rad) * this.radius * this.direction;
        } else {
            return Math.cos(this.degree / this.deg_to_rad) * this.radius * this.direction;
        }
    }
    
}