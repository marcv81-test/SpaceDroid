package com.test;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

class TestOrientation
{
    protected final Context context;

    protected final SensorManager sensorManager;
    protected final Sensor accelerometer;
    protected final Sensor magnetometer;

    protected float[] accelerometerValues = new float[3];
    protected float[] magnetometerValues = new float[3];

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

    protected final SensorEventListener magnetometerEventListener = new SensorEventListener()
    {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }

        @Override
        public void onSensorChanged(SensorEvent event)
        {
            for(int i=0; i<3; i++) magnetometerValues[i] = event.values[i];
        }
    };

    TestOrientation(Context context)
    {
        this.context = context;

        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void registerListeners()
    {
        if(accelerometer != null)
            sensorManager.registerListener(
                accelerometerEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        if(magnetometer != null)
            sensorManager.registerListener(
                magnetometerEventListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListeners()
    {
        if(accelerometer != null)
            sensorManager.unregisterListener(accelerometerEventListener);
        if(magnetometer != null)
            sensorManager.unregisterListener(magnetometerEventListener);
    }

    public float[] getRotationMatrix()
    {
        float[] rotationMatrix = new float[16];
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues, magnetometerValues);
        return rotationMatrix;
    }
}
