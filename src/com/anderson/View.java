package com.anderson;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class View {

    private final int height;
    private final int width;
    private Array2D map;
    List<Point2D> trail;
    
    public View(int height, int width) {
        this.height = height;
        this.width = width;
        this.reset_land();
    }

    public void reset_land() {
        Generate g1 = new DiamondSquare();
        Array2D map1 = g1.gen(this.height, this.width, 0.0f, 1.0f);

        Generate g2 = new PointyThings();
        Array2D map2 = g2.gen(this.height, this.width, 0.0f, 1.0f);

        this.map = Array2D.blend_average(map1, map2);
        this.reset_path();
    }

    public void reset_path() {
        int x1 = Util.next(0, map.width() - 1);
        int y1 = Util.next(0, map.height() - 1);
        int x2 = Util.next(0, map.width() - 1);
        int y2 = Util.next(0, map.height() - 1);
        Point2D start = new Point2D.Float(x1, y1);
        Point2D end = new Point2D.Float(x2, y2);

        this.trail = new ArrayList<Point2D>();
        float max_transition = 0.0f;
        while (this.trail.size() == 0) {
            PathMoveCostInterface cost = new PathMoveCostMaxTransition(this.map, start, end, max_transition);
            this.trail = Path.aStar(this.map, start, end, cost);
            max_transition += 0.0002f;
        }
    }

    public void draw(Graphics2D g, float width, float height) {
        double stepX = width / this.map.width();
        double stepY = height / this.map.height();

        for (int y = this.map.minY(); y <= this.map.maxY(); ++y) {
            for(int x = this.map.minX(); x <= this.map.maxX(); ++x) {
                float val = this.map.get(y, x);
                Color c = new Color(val, val, val);
                g.setColor(c);
                g.fillRect((int)(x * stepX), (int)(y * stepY), (int)stepX, (int)stepY);
            }
        }

        for (int i = 0; i < this.trail.size(); ++i) {
            Point2D p = this.trail.get(i);
            float val = 1.0f - this.map.get(p);
            Color c = new Color(val, 0, val);
            g.setColor(c);
            g.fillRect((int)(p.getX() * stepX), (int)(p.getY() * stepY), (int)stepX, (int)stepY);
        }
    }

    public void next_frame() {
        return;
    }
}
