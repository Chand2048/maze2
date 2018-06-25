package com.anderson;

public class DiamondSquare implements Generate {

    private Array2D map = null;
    private float min;
    private float max;

	public Array2D gen(int height, int width, float min, float max) {
        this.map = new Array2D(height, width, min, max);
        this.min = min;
        this.max = max;

        // Seed the four corners of the map.
        float a = Util.next(min, max);
        float b = Util.next(min, max);
        float c = Util.next(min, max);
        float d = Util.next(min, max);

        gen_internal(0.0f, 0.0f, height, width, a, b, c, d);
        //smooth();
        
        return map;
    }

    private float displace(float height, float width, float val) {
        // Scale the change based on how small this grid is.
        float dampening = ((width / this.map.width()) + (height / this.map.height())) / 2.0f;
        float radius = ((this.max - this.min) * dampening * 3.0f) / 2.0f; 
        float nextVal = Util.next(val - radius, val + radius);

        if (nextVal > this.max) {
            return this.max;
        } else if (nextVal < this.min) {
            return this.min;
        }

        return nextVal;
    }

    private void gen_internal(float y, float x, float height, float width, float a, float b, float c, float d) {
        // See https://en.wikipedia.org/wiki/Diamond-square_algorithm
        
        if (width > 1.0f || height > 1.0f)
        {
            float middle = this.displace(height, width, (a + b + c + d) / 4.0f);

            float top = (a + b) / 2.0f;
            float right = (b + c) / 2.0f;
            float bottom =  (c + d) / 2.0f;
            float left = (a + d) / 2.0f;
            // float top = this.displace(height, width, (a + b) / 2.0f);
            // float right = this.displace(height, width, (b + c) / 2.0f);
            // float bottom = this.displace(height, width, (c + d) / 2.0f);
            // float left = this.displace(height, width, (a + d) / 2.0f);

            float nextHeight = height / 2.0f;
            float nextWidth = width / 2.0f;

            gen_internal(y, x, nextHeight, nextWidth, a, top, middle, left); // upper left
            gen_internal(y, x + nextWidth, nextHeight, nextWidth, top, b, right, middle); // upper right
            gen_internal(y + nextHeight, x + nextWidth, nextHeight, nextWidth, middle, right, c, bottom); // lower right
            gen_internal(y + nextHeight, x, nextHeight, nextWidth, left, middle, bottom, d); // lower left
        } else {
            this.map.set((int)y, (int)x, (a + b + c + d) / 4.0f);
        }
    }
}