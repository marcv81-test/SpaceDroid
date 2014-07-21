package com.test;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

class TestRenderer implements GLSurfaceView.Renderer
{
    protected final GfxBlock background;
    protected final GfxBlock foreground;

    protected long startTime;
    float viewCoordinates[] = {0f, 0f};

    TestRenderer()
    {
        this.background = new GfxBlock(2f, 3f, 0f, 1f, 0f);
        this.foreground = new GfxBlock(0.5f, 0f, 0f, 0f, 1f);

        startTime = System.currentTimeMillis();
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

        GLU.gluLookAt(gl,
            viewCoordinates[0] / 4, viewCoordinates[1] / 4, -3f,
            viewCoordinates[0] / 4, viewCoordinates[1] / 4, 0f,
            0f, 1f, 0f);

        float elapsed = (System.currentTimeMillis() - startTime) / 1000f;

        background.draw(gl, 0f, 0f);
        foreground.draw(gl, 0.5f*(float)Math.cos(elapsed), 0.5f*(float)Math.sin(elapsed));
    }

    void updateView(float dx, float dy)
    {
        viewCoordinates[0] += dx;
        viewCoordinates[1] += dy;
    }
}
