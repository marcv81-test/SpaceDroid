package com.test;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

class TestView extends GLSurfaceView
{
    protected final TestRenderer renderer;

    TestView(Context context)
    {
        super(context);
        renderer = new TestRenderer(context);
        setRenderer(renderer);
    }

    private float previousX;
    private float previousY;

    private final float SCROLL_DIVIDER = 40f;

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, we are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = (x - previousX) / SCROLL_DIVIDER;
                float dy = (y - previousY) / SCROLL_DIVIDER;

                renderer.updateView(dx, dy);
            break;
        }

        previousX = x;
        previousY = y;

        return true;
    }
}
