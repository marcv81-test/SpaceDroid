package com.test;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

class TestRenderer implements GLSurfaceView.Renderer
{
    protected final Context context;
    protected final TestOrientation orientation;
    protected final TestShape shape = new TestShape();

    TestRenderer(Context context, TestOrientation orientation)
    {
        this.context = context;
        this.orientation = orientation;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        float ratio = (float) width / height;
        GLU.gluPerspective(gl, 45.0f, ratio, 1, 100f);
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        // Clear the screen to black
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Position model so we can see it
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadMatrixf(orientation.getRotationMatrix(), 0);
        gl.glTranslatef(0, 0, -3.0f);

         // Draw the model
         shape.draw(gl);
    }
}
