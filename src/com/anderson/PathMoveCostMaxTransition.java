package com.anderson;

import java.awt.geom.Point2D;

public final class PathMoveCostMaxTransition implements PathMoveCostInterface {

    private final Array2D terrain;
    private final Point2D start;
    private final Point2D end;
    private final float max_transition;

    public PathMoveCostMaxTransition(final Array2D terrain, final Point2D start, final Point2D end, final float max_transition) {
        this.terrain = terrain;
        this.start = start;
        this.end = end;
        this.max_transition = max_transition;
    }

    @Override
    public float calcCost(final Point2D current, final Point2D next, final float current_cost) {

        float transition = Math.abs(this.terrain.get(current) - this.terrain.get(next));

        // Do not allow moving to this cell if the elevation change is above the max.
        if (transition > max_transition) {
            return Float.MAX_VALUE;
        }

        return current_cost + transition;
    }
}
