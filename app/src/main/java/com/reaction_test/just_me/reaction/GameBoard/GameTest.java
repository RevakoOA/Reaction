package com.reaction_test.just_me.reaction.GameBoard;

/**
 * Created by just_me on 20.10.15.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.reaction_test.just_me.reaction.GameMenu;

public class GameTest extends Activity implements Runnable{

    Surface drawView;
    Context context;

    public static Boolean isPaused = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawView = new Surface(this);
        drawView.setBackgroundColor(Color.WHITE);
        setContentView(drawView);

        context = this;

        drawView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GameMenu.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeFullScreen();
        GameTest.isPaused = false;
    }

    @Override
    protected void onPause(){
        super.onPause();
        GameTest.isPaused = true;
    }

    @Override
    public void run() {
        while (true){
            try {
                if (!GameTest.isPaused)
                    drawView.postInvalidate();
                Thread.sleep(10);
            }catch (Exception e){

            }

        }
    }

    public void makeFullScreen(){
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else{
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = getActionBar();
            if (actionBar != null)
                actionBar.hide();
        }

    }
}
