package com.reaction_test.just_me.reaction.Settings;

/**
 * Created by just_me on 21.10.15.
 */
public class Settings {

    public static Settings mSettings;

    public final int runnersN;
    public final boolean invertedControl;
    public final double sensitivity;
    public final int level;

    public static void NewSetting(int runnersN, boolean invertedControl, double sensitivity, int level){
        mSettings = new Settings(runnersN,invertedControl,sensitivity,level);
    }

    private Settings(int runnersN, boolean invertedControl, double sensitivity, int level) {
        this.runnersN = runnersN;
        this.invertedControl = invertedControl;
        this.sensitivity = sensitivity;
        this.level = level;
    }
}
