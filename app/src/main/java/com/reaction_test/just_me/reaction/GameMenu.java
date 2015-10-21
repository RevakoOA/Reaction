package com.reaction_test.just_me.reaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by just_me on 20.10.15.
 */
public class GameMenu extends Activity {


    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_menu);

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Curely.ttf");
        setActions();
        registerListener();
        Log.d("TAG", "Game Menu started)");
    }

    private void registerListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Intent intent = new Intent(GameMenu.this, GameTest.class);
                    startActivity(intent);
                }
                if (position == 1){
                    finish();
                }
            }
        });
    }

    private void setActions() {
        String[] actions = {"New Game", "Continue", "Settings"};
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this, R.layout.game_menu_item, actions);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

}
