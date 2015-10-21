package com.reaction_test.just_me.reaction;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by just_me on 20.10.15.
 */
public class RotateReader implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor        mSensor;

    public float[] currentVector = new float[3];
    public float[] zeroVector    = {0, 0, 1};
    public int     direction     = 0;
    public double  force         = 0;

    public RotateReader(Context context){
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mSensor != null){
            Log.d("TAG", "Gyroscope is ready for use!");
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
        }else {
            Log.d("TAG", "There is no gyroscope on this device");
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mSensor != null) {
                Log.d("TAG", "Accelerometer is ready for use!");
                mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
            } else {
                Log.d("TAG", "There is no accelerometer on this device too. We have trouble with control");
            }
        }
    }

    public void calibrate(){
        zeroVector = Arrays.copyOf(currentVector, currentVector.length);
    }

    /**
     * @return angle between currentVector and zeroVector
     * The more device is rotated - the more angle.
     */
    public double getAngle(){
        double cLength = 0;
        double zLength = 0;
        double scalar  = 0;

        for (int i = 0; i < currentVector.length; i++) {
            cLength += Math.pow(currentVector[i], 2);
            zLength += Math.pow(zeroVector[i], 2);
            scalar  += currentVector[i] * zeroVector[i];
        }
        cLength = Math.sqrt(cLength);
        zLength = Math.sqrt(zLength);

        force     = Math.acos(scalar / cLength / zLength);
        force     = (force > 1)? 1: force;
        direction = (int) Math.toDegrees(Math.atan(currentVector[0]/currentVector[1]));
        direction += 90;
        direction += (currentVector[1] < 0)? 180: 0;


        return force;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        currentVector[0] = event.values[0];
        currentVector[1] = event.values[1];
        currentVector[2] = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
