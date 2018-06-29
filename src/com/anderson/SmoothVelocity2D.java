package com.anderson;

public class SmoothVelocity2D {

    private Smoother disturbanceX;
    private Smoother disturbanceY;
    private Smoother baseX;
    private Smoother baseY; 

    public SmoothVelocity2D(int minRadius, int maxRadius) {
        this.baseX = new Smoother(true, Util.next(minRadius, maxRadius), Util.next(1, 5));
        this.baseY = new Smoother(false, Util.next(minRadius, maxRadius), Util.next(1, 5));
        this.disturbanceX = new Smoother(false, Util.next(-2, 2), Util.next(1, 3));
        this.disturbanceY = new Smoother(true, Util.next(-2, 2), Util.next(1, 3));
    }

    public void next_frame() {
        this.baseX.next_frame();
        this.baseY.next_frame();
    }

    public void reverseX() {
        this.baseX.reverse();
        this.disturbanceX.reverse();
    }

    public void reverseY() {
        this.baseY.reverse();
        this.disturbanceY.reverse();
    }

	public float getX() {
        return this.baseX.get() + this.disturbanceX.get();
	}

	public float getY() {
        return this.baseY.get() + this.disturbanceY.get();
	}

	public void setLocation(float x, float y) {
        this.baseX.set_radius(x);
        this.baseY.set_radius(y);
	}
}