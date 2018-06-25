package com.anderson;

import java.awt.geom.Point2D;

public final class PointyThings implements Generate {
    @Override
    public Array2D gen(int height, int width, float min, float max) {
        Array2D map = new Array2D(height, width, min, max);
        map.reset(min);

        int longest_edge = Math.max(height, width);
        int point_count =  longest_edge / 15;
        Point2D[] points = new Point2D[point_count];
        float[] point_radius = new float[point_count];
        float[] point_height = new float[point_count];

        for (int i = 0; i < points.length; ++i) {
            points[i] = new Point2D.Float(Util.next(0f, (float)width), Util.next(0f, (float)height));
            point_radius[i] = Util.next(longest_edge / 20, longest_edge / 3);
            point_height[i] = (float)Util.next();
        }

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {

                Point2D current = new Point2D.Float(x, y);
                float max_elevation = min;

                for (int i = 0; i < points.length; ++i) {
                    float distance = (float)current.distance(points[i]);
                    if (distance > point_radius[i]) {
                        continue;
                    }

                    float elevation = point_height[i] * (point_radius[i] - distance) / point_radius[i];
                    if (elevation > max_elevation) {
                        max_elevation = elevation;
                    }
                }

                map.set(y, x, max_elevation);
            }
        }

        return map;
    }
}
