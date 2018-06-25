package com.anderson;

import java.awt.geom.Point2D;

public class SmoothVelocity extends Point2D {

    private Smoother disturbanceX;
    private Smoother disturbanceY;
    private Smoother baseX;
    private Smoother baseY; 

    public SmoothVelocity(int minRadius, int maxRadius) {
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

    @Override
	public double getX() {
        return this.baseX.get() + this.disturbanceX.get();
	}

	@Override
	public double getY() {
        return this.baseY.get() + this.disturbanceY.get();
	}

	@Override
	public void setLocation(double x, double y) {
        this.baseX.set_radius(x);
        this.baseY.set_radius(y);
	}
}