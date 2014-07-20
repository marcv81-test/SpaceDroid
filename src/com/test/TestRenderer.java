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
    protected final TestShape shape;

    TestRenderer(Context context, TestOrientation orientation)
    {
        this.context = context;
        this.orientation = orientation;
        this.shape = new TestShape();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        float coordinates[] = orientation.getOrientation();

        GLU.gluLookAt(gl,
            coordinates[0] / 4, coordinates[1] / 4, -3f,
            coordinates[0] / 4, coordinates[1] / 4, 0f,
            0f, 1f, 0f);

        shape.draw(gl);
    }
}
