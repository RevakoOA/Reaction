package com.reaction_test.just_me.reaction.Settings;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.reaction_test.just_me.reaction.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by just_me on 21.10.15.
 */
public class SettingsMenu extends Activity {
    List<SettingsItem> listSettings;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);

        populateList();
        populateListView();


    }

    private void populateList(){
        listSettings = new ArrayList<SettingsItem>();
        listSettings.add(new SettingsItem("Runners", 3));
        listSettings.add( new SettingsItem("Level", 10));
        listSettings.add(new SettingsItem("Sensitivity", 5));

    }

    private void populateListView(){
        SettingsAdapter adapter = new SettingsAdapter();
        listView = (ListView) findViewById(R.id.settingsList);
        listView.setAdapter(adapter);
    }

    private class SettingsItem {
        public String name;
        public int value;

        public SettingsItem(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }
    private class SettingsAdapter extends ArrayAdapter<SettingsItem>{

        public SettingsAdapter() {
            super(SettingsMenu.this, R.layout.settings_menu_item, listSettings);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = SettingsMenu.this.getLayoutInflater().
                        inflate(R.layout.settings_menu_item, null);
            }

            SettingsItem item = listSettings.get(position);

            TextView name = (TextView) itemView.findViewById(R.id.itemName);
            SeekBar  result = (SeekBar) itemView.findViewById(R.id.result);
            name.setText(item.name);
            result.setProgress(item.value);
            return itemView;
        }
    }
}
