package com.test;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

class TestRenderer implements GLSurfaceView.Renderer
{
    protected final Context context;
    protected Texture galaxyTexture;
    protected Texture planetTexture;
    protected Texture[] crashTexture = new Texture[8];

    protected final float FOREGROUND_PLANE = 0f;
    protected final float BACKGROUND_PLANE = 2f;
    protected final float INFINITY_PLANE = 8f;

    protected final GfxBlock galaxyBlock;
    protected final GfxBlock planetBlock;
    protected final GfxBlock satelliteBlock;
    protected final GfxBlock spaceshipBlock;

    protected long startTime;

    float viewCoordinates[] = {0f, 0f};

    TestRenderer(Context context)
    {
        this.context = context;
        this.galaxyTexture = new Texture();
        this.planetTexture = new Texture();
        this.crashTexture[0] = new Texture();
        this.crashTexture[1] = new Texture();
        this.crashTexture[2] = new Texture();
        this.crashTexture[3] = new Texture();
        this.crashTexture[4] = new Texture();
        this.crashTexture[5] = new Texture();
        this.crashTexture[6] = new Texture();
        this.crashTexture[7] = new Texture();

        this.galaxyBlock = new GfxBlock(16f, INFINITY_PLANE, galaxyTexture);
        this.planetBlock = new GfxBlock(1f, BACKGROUND_PLANE, planetTexture);
        this.satelliteBlock = new GfxBlock(0.3f, BACKGROUND_PLANE,
                planetTexture);
        this.spaceshipBlock = new GfxBlock(0.3f, FOREGROUND_PLANE,
                crashTexture[0]);

        startTime = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Enable blending using premultiplied alpha.
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

        galaxyTexture.load(gl, context, R.drawable.galaxy);
        planetTexture.load(gl, context, R.drawable.planet);
        crashTexture[0].load(gl, context, R.drawable.crash0);
        crashTexture[1].load(gl, context, R.drawable.crash1);
        crashTexture[2].load(gl, context, R.drawable.crash2);
        crashTexture[3].load(gl, context, R.drawable.crash3);
        crashTexture[4].load(gl, context, R.drawable.crash4);
        crashTexture[5].load(gl, context, R.drawable.crash5);
        crashTexture[6].load(gl, context, R.drawable.crash6);
        crashTexture[7].load(gl, context, R.drawable.crash7);

        gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping ( NEW )
        gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
        gl.glClearDepthf(1.0f); // Depth Buffer Setup
        gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
        gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 3, 20);
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

        galaxyBlock.draw(gl, 0f, 0f, 0f);
        planetBlock.draw(gl, 0f, 0f, -57.32f * elapsed);
        satelliteBlock.draw(gl, 0.8f * (float) Math.cos(elapsed),
                1f * (float) Math.sin(elapsed), 0f);
        spaceshipBlock.setTexture(crashTexture[(int) (elapsed * 12 % 8)]);
        spaceshipBlock.draw(gl, 0.5f, 0.5f, 0f);
    }

    void updateView(float dx, float dy)
    {
        viewCoordinates[0] += dx;
        viewCoordinates[1] += dy;
    }
}
