package com.anderson;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class Geography {
    Array2D red;
    Array2D green;
    Array2D blue;

    DiamondSquare ds;
    int height;
    int width;

    SmoothVelocity vred;
    SmoothVelocity vgreen;
    SmoothVelocity vblue;

    Point2D cred;
    Point2D cgreen;
    Point2D cblue;

    public Geography(int height, int width) {
        this.height = height;
        this.width = width;

        this.vred = new SmoothVelocity(0, 2);
        this.vgreen = new SmoothVelocity(0, 2);
        this.vblue = new SmoothVelocity(0, 2);

        this.cred = new Point2D.Float(0.0f, 0.0f);
        this.cgreen = new Point2D.Float(0.0f, 0.0f);
        this.cblue = new Point2D.Float(0.0f, 0.0f);

        DiamondSquare ds = new DiamondSquare();
        this.red = ds.gen(this.height, this.width, 0.0f, 1.0f);
        this.green = ds.gen(this.height, this.width, 0.0f, 1.0f);
        this.blue = ds.gen(this.height, this.width, 0.0f, 1.0f);
    }

    public void next_frame() {
        move_color(this.cred, this.vred);
        move_color(this.cgreen, this.vgreen);
        move_color(this.cblue, this.vblue);
    }

    private void move_color(Point2D p, SmoothVelocity v) {
        v.next_frame();

        double x = p.getX() + v.getX();
        double y = p.getY() + v.getY();

        if (x > this.width - 1) {
            x = this.width - 1;
            v.reverseX();
        } else if (x < 0) {
            x = 0;
            v.reverseX();
        }

        if (y > this.height - 1) {
            y = this.height - 1;
            v.reverseY();
        } else if (y < 0) {
            y = 0;
            v.reverseY();
        }

        p.setLocation(x, y);
    }

    public void reset() {
        DiamondSquare ds = new DiamondSquare();
        this.red = ds.gen(this.height, this.width, 0.0f, 1.0f);
        this.green = ds.gen(this.height, this.width, 0.0f, 1.0f);
        this.blue = ds.gen(this.height, this.width, 0.0f, 1.0f);
    }

    public void draw(Graphics2D g, float height, float width) {
        Array2D red_l = this.red;
        Array2D green_l = this.green;
        Array2D blue_l = this.blue;
        double stepX = width / red_l.width();
        double stepY = height / red_l.height();

        for (int y = red_l.minY(); y <= red_l.maxY(); ++y) {
            for(int x = red_l.minX(); x <= red_l.maxX(); ++x) {
                float r = red_l.get_wrap((int) (y + this.cred.getY()), (int) (x + this.cred.getX()));
                float gr = green_l.get_wrap((int) (y + this.cgreen.getY()), (int) (x + this.cgreen.getX()));
                float b = blue_l.get_wrap((int) (y + this.cblue.getY()), (int) (x + this.cblue.getX()));
                Color c = new Color(r,gr,b);
                g.setColor(c);
                g.fillRect((int)(x * stepX), (int)(y * stepY), (int)stepX, (int)stepY);
            }
        }
    }
}