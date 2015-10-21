package com.reaction_test.just_me.reaction.GameBoard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.reaction_test.just_me.reaction.Settings.Settings;

import java.util.Random;

/**
 * Surface represent canvas where game is painted.
 */
public class Surface extends View implements Runnable {

    //display resolution in plain pixels
    int dWidth;
    int dHeight;

    // runners like a bullies, you should avoid them
    Runner[] runners;
    //Run forest, run ^) controlled runner
    Runner forest;
    //Controller for forest
    RotateReader rr;

    //for drawing triangles
    private Path path;

    //just pointer for one of paints
    private Paint paint;

    //different paints with different colors for runners
    private Paint runnerPaint = new Paint();
    // for forest
    private Paint forestPaint = new Paint();
    // for everything else
    private Paint defaultPaint = new Paint();

    //random for different shapes of runners each game
    private Random random = new Random();

    public Surface(Context context) {
        super(context);
        runnerPaint.setColor(Color.BLUE);
        forestPaint.setColor(Color.GREEN);
        defaultPaint.setColor(Color.MAGENTA);
        paint = defaultPaint;

        path = new Path();

        measureScreen(context);
        setRunnersForStart();
        //start thread for
        new Thread(this).start();
        rr = new RotateReader(context);
        Log.d("TAG", Integer.toString(dWidth) + "; " + Integer.toString(dHeight));
    }

    /**
     * Method for resolving display resolution
     * @param context
     */
    public void measureScreen(Context context){
        WindowManager wm;
        Display display;
        Point size = new Point();
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
    }
    /**
     * Everything is painted here.
     * @param canvas - painter from system
     */
    @Override
    public void onDraw(Canvas canvas) {
        paint = defaultPaint;
        rr.getAngle();
        canvas.drawText(Double.toString(rr.force), 10, 10, paint);
        canvas.drawText(Integer.toString(rr.direction), 10, 30, paint);
        canvas.drawLine(dWidth/2, dHeight/2,
                (float)(dWidth/2  + dHeight/2 * rr.force * Math.sin(Math.toRadians(rr.direction))),
                (float)(dHeight/2 - dHeight/2 * rr.force * Math.cos(Math.toRadians(rr.direction))),
                paint);

        nextPosition();
        Runner runner;
        for (int i = 0; i <= runners.length; i++) {
            if (i < runners.length){
                runner = runners[i];
                paint = runnerPaint;
            }else{
                runner = forest;
                paint  = forestPaint;
            }

            if (runner.shape == Runner.SHAPE_RECT){
                canvas.drawRect((float)runner.position.x, (float)runner.position.y,
                        (float)runner.width + runner.position.x, (float)runner.heigh + runner.position.y, paint);
            }
            if (runner.shape == Runner.SHAPE_ROUND){
                canvas.drawText(Integer.toString(i), runner.position.x, runner.position.y, paint);
                canvas.drawCircle((float) runner.position.x + runner.width / 2, (float) runner.position.y + runner.heigh / 2,
                        runner.width / 2, paint);
            }
            if (runner.shape == Runner.SHAPE_TRIANGLE){
                path.reset();
                path.setFillType(Path.FillType.EVEN_ODD);
                path.moveTo(runner.position.x, runner.position.y + runner.heigh);
                path.lineTo(runner.position.x + runner.width / 2, runner.position.y);
                path.lineTo(runner.position.x+runner.width, runner.position.y+runner.heigh);
                path.close();
                canvas.drawPath(path, paint);
            }
        }
        canvas.drawLine(0, 0, dWidth, dHeight, paint);
    }

    /**
     * Compute next position of each runners (controlled too)
     */
    public void nextPosition(){
        Runner runner;
        double dx, dy;

        for (int i = 0; i <= runners.length; i++) {
            runner = (i < runners.length)? runners[i]: forest;

            if (runner != forest) {
                //get direction from runner
                //TODO should be some optimization
                dy =  runner.velocity * Math.cos(Math.toRadians(runner.direction));
                dx =  -runner.velocity * Math.sin(Math.toRadians(runner.direction));
                if (Settings.mSettings.invertedControl){
                    dx *= -1;
                    dy *= -1;
                }
            }else{
                //get direction from controller
                //TODO implements sensitivity here
                dy = +runner.velocity * Math.pow(Math.E, rr.force*5)
                        * Math.cos(Math.toRadians(rr.direction));
                dx = -runner.velocity * Math.pow(Math.E, rr.force*5)
                        * Math.sin(Math.toRadians(rr.direction));
            }
            runner.position.set((int) Math.round(runner.position.x + dx),
                                (int) Math.round(runner.position.y + dy));

            //Make new direction in case of rebound from the wall (4 wall - 4 if-statements)
            if (runner.position.x > dWidth-runner.width) {
                runner.position.x = dWidth - runner.width - 1;
                runner.nextDirection(190, 350);
            }
            else if (runner.position.x < 0) {
                runner.position.x = 1;
                runner.nextDirection(10, 170);
            }
            if (runner.position.y > dHeight-runner.heigh) {
                runner.position.y = dHeight-runner.heigh - 1;
                runner.nextDirection(280, 440);
            }
            else if (runner.position.y < 0) {
                runner.position.y = 1;
                runner.nextDirection(100, 260);
            }
            //check for stroking runners wit forest
            if (runner != forest){
                //TODO for others shapes
                if (runner.shape == Runner.SHAPE_ROUND)
                if (Math.sqrt(Math.pow(runner.position.x + runner.width/2 - forest.position.x - forest.width/2, 2)+
                    Math.pow(runner.position.y + runner.heigh/2 - forest.position.y - forest.heigh/2, 2)) < runner.width/2 + forest.width/2){
                    GameTest.isPaused = true;
                    Log.d("TAG", "GAme over."+Integer.toString(i));
                }
            }
        }
    }

    /**
     * Method prepares runners for start game.
     */
    public void setRunnersForStart(){

        //special parameters for controlled item
        forest = new Runner(Runner.SHAPE_ROUND, Math.min(dWidth, dHeight) / 7, Math.min(dWidth, dHeight) / 7);
        forest.velocity = 5;
        forest.position.set(dWidth / 2, dHeight / 2);


        // create runners
        runners = new Runner[Settings.mSettings.runnersN];

        //set shapes for each runner and start direction of moving
        for (int i = 0; i < runners.length; i++) {
            runners[i] = new Runner(Runner.SHAPE_ROUND, dWidth / 6, dHeight / 6);
            runners[i].nextDirection(0, 360);
        }

        //Set coordinates where runners will be placed at the beginning of the game

        runners[0].position.set(0,0);
        //runners[1].position.set(dWidth-runners[1].width,0);
        //runners[2].position.set(0, dHeight-runners[2].heigh);
        //runners[3].position.set(dWidth-runners[1].width,dHeight-runners[2].heigh);
    }

    /**
     * This thread makes runners faster with some period.
     */
    @Override
    public void run() {
        int period = 30000 - (Settings.mSettings.level - 1) * 3000;
        try{
        while (true){

                if (!GameTest.isPaused)
                    for(Runner runner: runners){
                        runner.velocity += 1;
                    }
                Thread.sleep(period);
        }
        }catch (Exception e){
            //TODO write something here)
        }
    }
}
