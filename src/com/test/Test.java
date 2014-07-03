package com.test;

import android.app.Activity;
import android.os.Bundle;

public class Test extends Activity
{
    protected TestView view;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        view = new TestView(this);
        setContentView(view);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        view.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        view.onResume();
    }
}
