package com.anderson;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class View {
    
    private Array2D map;
    List<Point2D> trail;
    
    public View(int width, int height) {
        DiamondSquare ds = new DiamondSquare();
        this.map = ds.gen(width, height, 0.0f, 1.0f);
        this.reset();
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

    public void reset() {
        int x1 = Util.next(0, map.width() - 1);
        int y1 = Util.next(0, map.height() - 1);
        int x2 = Util.next(0, map.width() - 1);
        int y2 = Util.next(0, map.height() - 1);

        this.trail = new ArrayList<Point2D>();
        float max_transition = 0.0f;
        while (this.trail.size() == 0) {
            this.trail = Path.astar(this.map, y1, x1, y2, x2, max_transition);
            max_transition += 0.001f;
        }
    }

    public void next_frame() {
        return;
    }
}
