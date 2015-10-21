package com.reaction_test.just_me.reaction.GameBoard;

import android.graphics.Point;

import java.util.Map;
import java.util.Random;

/**
 * Created by just_me on 20.10.15.
 */
public class Runner {
    public static int SHAPE_ROUND = 0;
    public static int SHAPE_TRIANGLE = 1;
    public static int SHAPE_RECT = 2;

    // represented by angle [0; 360) degrees
    public int direction;
    public double velocity = 6;
    public int shape;
    public int width;
    public int heigh;

    //store information for drawing
    public Point position;

    //for choose next random direction
    private static Random random = new Random();

    public Runner(int shape, int width, int heigh) {
        this.shape = shape;
        this.width = width;
        this.heigh = heigh;
        position = new Point();
    }

    /**
     * Change runner direction.
     * @param from - beginning angle
     * @param to   - ending angle
     */
    public void nextDirection(int from, int to){
        while (from > to){
            to += 360;
        }
        this.direction = from + random.nextInt(to - from);
        while(direction < 0)
            direction += 360;
        while (direction > 360)
            direction -= 360;
    }
}
