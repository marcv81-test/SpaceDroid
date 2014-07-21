package com.test;

import android.content.Context;
import android.opengl.GLSurfaceView;

class TestView extends GLSurfaceView
{
    protected final TestRenderer renderer;

    TestView(Context context)
    {
        super(context);
        renderer = new TestRenderer();
        setRenderer(renderer);
    }
}
