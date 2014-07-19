package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import java.lang.Thread.UncaughtExceptionHandler;

public class Test extends Activity implements UncaughtExceptionHandler
{
    protected static final String TAG = "AndroidTest";

    protected TestView view;
    protected TestOrientation orientation;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.i(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(this);

        orientation = new TestOrientation(this);

        view = new TestView(this, orientation);
        setContentView(view);
    }

    @Override
    protected void onPause()
    {
        Log.i(TAG, "onPause()");

        super.onPause();
        orientation.unregisterListeners();
        view.onPause();
    }

    @Override
    protected void onResume()
    {
        Log.i(TAG, "onResume()");

        super.onResume();
        orientation.registerListeners();
        view.onResume();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable)
    {
        Log.w(TAG, "Caught: " + throwable.getClass());
        String message = throwable.getMessage();
        if(message != null) Log.w(TAG, message);

        System.exit(1);
    }
}

class TestException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public TestException(String s) { super(s); }
}
