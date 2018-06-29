package com.anderson;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class View {

    private final int height;
    private final int width;
    private Array2D map;
    private List<List<Point2D>> trails;
    private ColorRamp pathBlender;
    private ColorRamp backgroundBlender;
    
    public View(int height, int width) {
        this.height = height;
        this.width = width;
        this.reset_land();
    }

    public void reset_land() {
        Generate g1 = new DiamondSquare();
        Array2D dest = null;
        for (int i = 0; i < 3; ++i) {
            Array2D temp = g1.gen(this.height, this.width, 0.0f, 1.0f);
            if (dest != null) {
                dest = Array2D.blend_average(dest, temp);
            } else {
                dest = temp;
            }
        }

        Generate g2 = new PointyThings();
        Array2D map3 = g2.gen(this.height, this.width, 0.0f, 1.0f);
        map3.maximizeRange();

        this.pathBlender = new ColorRamp();
        this.backgroundBlender = new ColorRamp();

        this.map = Array2D.blend_average(dest, map3);
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
        PathMoveCostInterface cost = new PathMoveCostDownhill(this.map, start, end);
        List<Point2D> trail = Path.aStar(this.map, start, end, cost);
        this.trails.add(trail);
        this.recursivePath(trail, 1f);
    }

    private void recursivePath(List<Point2D> prevTrail, float branchChance) {
        while (Util.next() <= branchChance) {
            // Pick random point on the trail.
            int i = Util.next(0, prevTrail.size() - 1);
            Point2D start = prevTrail.get(i);
            Point2D end = this.randomPointOnMap();

            PathMoveCostInterface cost = new PathMoveCostDownhill(this.map, start, end);
            List<Point2D> trail = Path.aStar(this.map, start, end, cost);
            this.trails.add(trail);
            branchChance *= 0.7f;

            this.recursivePath(trail, branchChance);
        }
    }

    public void draw(Graphics2D g, int width, int height) {
        int stepX = width / this.map.width();
        int stepY = height / this.map.height();

        for (int y = this.map.minY(); y <= this.map.maxY(); ++y) {
            for(int x = this.map.minX(); x <= this.map.maxX(); ++x) {
                float val = this.map.get(x, y);
                g.setColor(this.backgroundBlender.get(val));
                g.fillRect(x * stepX, y * stepY, stepX, stepY);
            }
        }

        for (int j = 0; j < this.trails.size(); ++j) {
            float colorIndex = (j + 1.0f) / this.trails.size();

            List<Point2D> trail = this.trails.get(j);
            for (int i = 0; i < trail.size(); ++i) {
                Point2D p = trail.get(i);
                g.setColor(this.pathBlender.get(colorIndex));
                int x = (int) p.getX() * stepX;
                int y = (int) p.getY() * stepY;
                int scale = (int)(i * 0.01f);
                if (scale < 2) scale = 2;
                g.fillOval(x, y,stepX * scale, stepY * scale);
            }
        }
    }

    public void next_frame() {
        this.pathBlender.next_frame();
        this.backgroundBlender.next_frame();
    }
}
