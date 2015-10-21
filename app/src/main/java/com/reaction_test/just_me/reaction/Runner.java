package com.reaction_test.just_me.reaction;

import android.graphics.Point;

import java.util.Map;
import java.util.Random;

/**
 * Created by just_me on 20.10.15.
 */
public class Runner {
    private static Random random = new Random();
    public static int SHAPE_ROUND = 0;
    public static int SHAPE_TRIANGLE = 1;
    public static int SHAPE_RECT = 2;
    public int direction;
    public double velocity = 6;
    public int shape;
    public int width;
    public int heigh;
    public Point position;

    public Runner(int shape, int width, int heigh) {
        this.shape = shape;
        this.width = width;
        this.heigh = heigh;
        position = new Point();
    }

    public void nextDirection(int from, int to){
        this.direction = from + random.nextInt(to - from);
        while(direction < 0)
            direction += 360;
        while (direction > 360)
            direction -= 360;
    }
}
