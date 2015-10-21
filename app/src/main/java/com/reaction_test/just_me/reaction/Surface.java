package com.reaction_test.just_me.reaction;

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
import java.util.Random;

/**
 * Created by just_me on 20.10.15.
 */
public class Surface extends View implements Runnable {

    WindowManager wm;
    Display display;
    Point size = new Point();
    int dWidth;
    int dHeight;

    Runner [] runners = new Runner[4];
    Runner forest;
    RotateReader rr;
    private Random random = new Random();
    private Path path;
    private Paint paint;
    private Paint runnerPaint = new Paint();
    private Paint forestPaint = new Paint();
    private Paint defaultPaint = new Paint();
    public Surface(Context context) {
        super(context);
        runnerPaint.setColor(Color.BLUE);
        forestPaint.setColor(Color.GREEN);
        defaultPaint.setColor(Color.MAGENTA);
        paint = defaultPaint;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        path = new Path();
        setRunnersForStart();
        new Thread(this).start();
        rr = new RotateReader(context);
        Log.d("TAG", Integer.toString(dWidth) + "; " + Integer.toString(dHeight));
    }

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

    public void nextPosition(){
        Runner runner;
        double dy;
        double dx;
        for (int i = 0; i <= runners.length; i++) {
            runner = (i < runners.length)? runners[i]: forest;
            if (runner != forest) {
                dy = -runner.velocity * Math.cos(Math.toRadians(runner.direction));
                dx = runner.velocity * Math.sin(Math.toRadians(runner.direction));
            }else{
                dy = -runner.velocity * rr.force * Math.cos(Math.toRadians(rr.direction));
                dx =  runner.velocity * rr.force * Math.sin(Math.toRadians(rr.direction));
            }
            runner.position.set((int) Math.round(runner.position.x + dx), (int) Math.round(runner.position.y + dy));
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
        }
    }

    public void setRunnersForStart(){
        forest = new Runner(Runner.SHAPE_ROUND, Math.min(dWidth, dHeight) / 7, Math.min(dWidth, dHeight) / 7);
        forest.velocity = 20;
        forest.position.set(dWidth / 2, dHeight / 2);
        for (int i = 0; i < runners.length; i++) {
            runners[i] = new Runner(random.nextInt(3), dWidth / 6, dHeight / 6);
            runners[i].nextDirection(0, 360);
            runners[i].position.set(random.nextInt(dWidth - runners[i].width), random.nextInt(dHeight - runners[i].heigh));
        }
    }

    @Override
    public void run() {
        while (true){
            try{
                if (!GameTest.isPaused)
                    for (int i = 0; i < runners.length; i++) {
                        runners[i].velocity += 1;
                    }
                Thread.sleep(3000);
            }catch (Exception e){

            }
        }
    }
}
