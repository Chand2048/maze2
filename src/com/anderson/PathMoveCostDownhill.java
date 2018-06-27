package com.anderson;

import java.awt.geom.Point2D;

public final class PathMoveCostDownhill implements PathMoveCostInterface {

    private final Array2D terrain;
    private final Point2D start;
    private final Point2D end;

    public PathMoveCostDownhill(final Array2D terrain, final Point2D start, final Point2D end) {
        this.terrain = terrain;
        this.start = start;
        this.end = end;
    }

    @Override
    public float calcCost(final Point2D current, final Point2D next, final float current_cost) {
        //float transition = this.terrain.get(next) - this.terrain.get(current);
        //return current_cost + transition;
        return this.terrain.get(next);
    }
}
