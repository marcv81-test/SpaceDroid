package com.test;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

class TestOrientation
{
    protected static final String TAG = "AndroidTest";

    protected final Context context;

    protected final SensorManager sensorManager;
    protected final Sensor accelerometer;

    protected float[] accelerometerValues = new float[3];

    protected final SensorEventListener accelerometerEventListener = new SensorEventListener()
    {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }

        @Override
        public void onSensorChanged(SensorEvent event)
        {
            for(int i=0; i<3; i++) accelerometerValues[i] = event.values[i];
        }
    };

    TestOrientation(Context context)
    {
        this.context = context;

        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void registerListeners()
    {
        Log.i(TAG, "registerListeners()");

        sensorManager.registerListener(
            accelerometerEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListeners()
    {
        Log.i(TAG, "unregisterListeners()");

        sensorManager.unregisterListener(accelerometerEventListener);
    }

    public float[] getOrientation()
    {
        float[] result = new float[2];
        result[0] = accelerometerValues[0];
        result[1] = -accelerometerValues[1];
        return result;
    }
}
