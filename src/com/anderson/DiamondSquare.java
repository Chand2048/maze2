package com.anderson;

public class DiamondSquare implements Generate {

    private Array2D map = null;
    private float min;
    private float max;

	public Array2D gen(int width, int height, float min, float max) {
        this.map = new Array2D(width, height, min, max);
        this.min = min;
        this.max = max;

        // Seed the four corners of the map.
        float a = Util.next(min, max);
        float b = Util.next(min, max);
        float c = Util.next(min, max);
        float d = Util.next(min, max);

        gen_internal(0.0f, 0.0f, width, height, a, b, c, d);
        //smooth();
        
        return map;
    }

    private float displace(float width, float height, float val) {
        // Scale the change based on how small this grid is.
        float dampening = (height / this.map.width()) + (width / this.map.width()) * 2.5f;
        float radius = (this.max - this.min) * dampening;
        float nextVal = Util.next(val - radius, val + radius);

        if (nextVal > this.max) {
            return this.max;
        } else if (nextVal < this.min) {
            return this.min;
        }

        return nextVal;
    }

    private void gen_internal(float x, float y, float width, float height, float a, float b, float c, float d) {
        // See https://en.wikipedia.org/wiki/Diamond-square_algorithm
        
        if (width > 1.0f && height > 1.0f)
        {
            float middle = this.displace(width, height, (a + b + c + d) / 4.0f);

            float top = (a + b) / 2.0f;
            float right = (b + c) / 2.0f;
            float bottom =  (c + d) / 2.0f;
            float left = (a + d) / 2.0f;
            float nextHeight = height / 2.0f;
            float nextWidth = width / 2.0f;

            gen_internal(x, y, nextWidth, nextHeight, a, top, middle, left); // upper left
            gen_internal(x, y + nextHeight, nextWidth, nextHeight, top, b, right, middle); // upper right
            gen_internal(x + nextWidth, y + nextHeight, nextWidth, nextHeight, middle, right, c, bottom); // lower right
            gen_internal(x + nextWidth, y, nextWidth, nextHeight, left, middle, bottom, d); // lower left
        } else {
            this.map.set((int)x, (int)y, (a + b + c + d) / 4.0f);
        }
    }
}