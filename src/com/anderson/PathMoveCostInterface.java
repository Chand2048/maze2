package com.anderson;

import java.awt.geom.Point2D;

public interface PathMoveCostInterface {
    float calcCost(final Point2D current, final Point2D next, final float current_cost);
}
