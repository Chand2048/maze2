package com.anderson;

import java.awt.geom.Point2D;
import java.util.*;

public class Path {

    private static final Point2D[] offsets = new Point2D[] {
        new Point2D.Float(0.0f, -1.0f), // top
        new Point2D.Float(1.0f, 0.0f),  // right
        new Point2D.Float(0.0f, 1.0f),  // bottom
        new Point2D.Float(-1.0f, 0.0f)  // left
    };

    public static List<Point2D> aStar(Array2D terrain, Point2D start, Point2D end, PathMoveCostInterface cost) {
        PriorityQueue<PointVal> candidates = new PriorityQueue<PointVal>();
        Array2D visited = new Array2D(terrain.width(), terrain.height());
        visited.reset(Float.MAX_VALUE);
        Array2D breadcrumb = new Array2D(terrain.width(), terrain.height());
        breadcrumb.reset(-1.0f);

        // Set the start point to visited and point to itself
        candidates.add(new PointVal(start, 0));
        visited.set(start, 0.0f);
        breadcrumb.set(start, breadcrumb.convert(start));
        boolean found = false;

        while (!found && candidates.size() > 0) {
            PointVal current = candidates.poll();
            float current_cost = visited.get(current.p);
            if (current_cost == Float.MAX_VALUE) {
                current_cost = 0.0f;
            }

            for (int i = 0; i < offsets.length; ++ i) {
                Point2D next = new Point2D.Float(
                        (float)(current.p.getX() + offsets[i].getX()),
                        (float)(current.p.getY() + offsets[i].getY()));

                // Skip this if it is out of bounds or we have already visited it.
                if (!terrain.in_bounds(next)) {
                    continue;
                } else if (visited.get(next) != Float.MAX_VALUE) {
                    continue;
                }

                // Max cost means it is too high to visit.
                float next_cost = cost.calcCost(current.p, next, current_cost);
                if (next_cost == Float.MAX_VALUE) {
                    continue;
                }

                visited.set(next, next_cost);
                breadcrumb.set(next, breadcrumb.convert(current.p));
                candidates.add(new PointVal(next, next_cost));

                if (next.equals(end)) {
                    found = true;
                    break;
                }
            }
        }

        List<Point2D> trail = new ArrayList<Point2D>();
        if (found) {
            // Breadcrumbs are backwards so build array first
            List<Integer> backwards = new ArrayList<Integer>();
            int i = breadcrumb.convert(end);
            int start_index = breadcrumb.convert(start);
            while (i != start_index) {
                backwards.add(i);
                i = (int)breadcrumb.get(i);
            }

            // Populate output backwards while converting.
            trail.add(start);
            for (i = backwards.size() -1; i >= 0; --i) {
                trail.add(breadcrumb.convert(backwards.get(i)));
            }
        }

        return trail;
    }

    private static class PointVal implements Comparator, Comparable {
        public final Point2D p;
        public final float val;

        public PointVal(Point2D p, float val) {
            this.p = p;
            this.val = val;
        }

        public PointVal(float x, float y, float val) {
            this.p = new Point2D.Float(x, y);
            this.val = val;
        }

        @Override
        public int compare(Object o1, Object o2) {
            PointVal p1 = (PointVal)o1;
            PointVal p2 = (PointVal)o2;
            return Float.compare(p1.val, p2.val);
        }

        @Override
        public int compareTo(Object o) {
            PointVal p = (PointVal)o;
            return Float.compare(this.val, p.val);
        }
    }

}
