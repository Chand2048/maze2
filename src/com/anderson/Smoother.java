package com.anderson;

public class Smoother {
    private final float deg_to_rad = 57.29576f;

    private float radius;
    private float degree;
    private float speed;
    private boolean isSin;
    private float direction;

    public Smoother(boolean isSin, float radius, float speed) {
        this.isSin = isSin;
        this.degree = 0;
        this.radius = radius;
        this.speed = speed;
        this.direction = 1.0f;
    }

    public void reverse() {
        this.direction = this.direction * -1;
    }

    public void set_radius(float radius) {
        this.radius = radius;
    }

    public void next_frame() {
        this.degree += this.speed;
        if (this.degree > 360.0) {
            this.degree = this.degree % 360.0f;
        }
    }

    public float get() {
        if (this.isSin) {
            return (float)Math.sin(this.degree / this.deg_to_rad) * this.radius * this.direction;
        } else {
            return (float)Math.cos(this.degree / this.deg_to_rad) * this.radius * this.direction;
        }
    }
    
}