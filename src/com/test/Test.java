package com.test;

import android.app.Activity;
import android.os.Bundle;

public class Test extends Activity
{
    protected TestView view;
    protected TestOrientation orientation;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        orientation = new TestOrientation(this);

        view = new TestView(this, orientation);
        setContentView(view);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        orientation.unregisterListeners();
        view.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        orientation.registerListeners();
        view.onResume();
    }
}
