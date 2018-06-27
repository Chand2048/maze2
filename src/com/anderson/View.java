package com.anderson;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class View {

    private final int height;
    private final int width;
    private Array2D map;
    List<List<Point2D>> trails;
    
    public View(int height, int width) {
        this.height = height;
        this.width = width;
        this.reset_land();
    }

    public void reset_land() {
        Generate g1 = new DiamondSquare();
        Array2D map1 = g1.gen(this.height, this.width, 0.0f, 1.0f);
        Array2D map2 = g1.gen(this.height, this.width, 0.0f, 1.0f);

        Generate g2 = new PointyThings();
        Array2D map3 = g2.gen(this.height, this.width, 0.0f, 1.0f);

        this.map = Array2D.blend_average(map1, map2);
        this.map = Array2D.blend_average(map, map3);
        this.map.maximizeRange();
        this.reset_path();
    }

    private Point2D randomPointOnMap() {
        return new Point2D.Float(Util.next(0, map.width() - 1), Util.next(0, map.height() - 1));
    }

    public void reset_path() {
        Point2D start = this.randomPointOnMap();
        Point2D end = this.randomPointOnMap();

        this.trails = new ArrayList<>();
        PathMoveCostInterface cost = new PathMoveCostElevationChange(this.map, start, end);
        List<Point2D> trail = Path.aStar(this.map, start, end, cost);
        this.trails.add(trail);
        this.recursivePath(trail, 1f);
    }

    private void recursivePath(List<Point2D> prevTrail, float branchChance) {
        float localChance = branchChance;
        while (Util.next() <= branchChance) {
            // Pick random point on the trail.
            int i = Util.next(0, prevTrail.size() - 1);
            Point2D start = prevTrail.get(i);
            Point2D end = this.randomPointOnMap();

            PathMoveCostInterface cost = new PathMoveCostElevationChange(this.map, start, end);
            List<Point2D> trail = Path.aStar(this.map, start, end, cost);
            this.trails.add(trail);
            branchChance *= 0.9f;

            this.recursivePath(trail, branchChance * 0.9f);
        }
    }

    public void draw(Graphics2D g, int width, int height) {
        int stepX = width / this.map.width();
        int stepY = height / this.map.height();

        for (int y = this.map.minY(); y <= this.map.maxY(); ++y) {
            for(int x = this.map.minX(); x <= this.map.maxX(); ++x) {
                float val = this.map.get(x, y);
                Color c = new Color(val, val, val);
                g.setColor(c);
                g.fillRect(x * stepX, y * stepY, stepX, stepY);
            }
        }

        for (int j = 0; j < this.trails.size(); ++j) {
            List<Point2D> trail = this.trails.get(j);
            for (int i = 0; i < trail.size(); ++i) {
                Point2D p = trail.get(i);
                float val = 1.0f - this.map.get(p);
                Color c = new Color(val, 0, val);
                g.setColor(c);
                g.fillRect((int) p.getX() * stepX, (int) p.getY() * stepY, stepX, stepY);
            }
        }
    }

    public void next_frame() {
        return;
    }
}
