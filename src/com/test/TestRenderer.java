package com.test;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

class TestRenderer implements GLSurfaceView.Renderer
{
    protected final Context context;
    protected final TestShape shape = new TestShape();

    protected long startTime;

    TestRenderer(Context context)
    {
        this.context = context;
        startTime = System.currentTimeMillis();
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
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -3.0f);

        // Set rotation angle based on the time
        long elapsed = System.currentTimeMillis() - startTime;
        gl.glRotatef(elapsed * (30f / 1000f), 0, 1, 0);
        gl.glRotatef(elapsed * (15f / 1000f), 1, 0, 0);

         // Draw the model
         shape.draw(gl);
    }
}
