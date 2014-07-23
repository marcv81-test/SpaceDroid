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
    protected Texture fireballTexture;

    protected final float FOREGROUND_PLANE = 0f;
    protected final float BACKGROUND_PLANE = 2f;
    protected final float INFINITY_PLANE = 8f;

	protected final Sprite galaxySprite;
	protected final Sprite planetSprite;
	protected final Sprite satelliteSprite;
	protected final Sprite fireballSprite;

    protected long startTime;

    float viewCoordinates[] = {0f, 0f};

    TestRenderer(Context context)
    {
        this.context = context;
		this.galaxyTexture = new Texture(1, 1);
		this.planetTexture = new Texture(1, 1);
		this.fireballTexture = new Texture(5, 5);

		this.galaxySprite = new Sprite(0f, 0f, INFINITY_PLANE, 0f, 16f,
				galaxyTexture);
		this.planetSprite = new Sprite(0f, 0f, BACKGROUND_PLANE, 0f, 1f,
				planetTexture);
		this.satelliteSprite = new Sprite(0f, 0f, BACKGROUND_PLANE, 0f, 0.3f,
                planetTexture);
		this.fireballSprite = new Sprite(0f, 0f, FOREGROUND_PLANE, 0f, 0.3f,
				fireballTexture);

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
		fireballTexture.load(gl, context, R.drawable.fireball);

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

		planetSprite.setAngle(-57.32f * elapsed);
		satelliteSprite.setXY(0.8f * (float) Math.cos(elapsed),
				1.2f * (float) Math.sin(elapsed));
		fireballSprite.setAnimation((int) (elapsed * 12 % 25));

		galaxySprite.draw(gl);
		planetSprite.draw(gl);
		satelliteSprite.draw(gl);
		fireballSprite.draw(gl);
    }

    void updateView(float dx, float dy)
    {
        viewCoordinates[0] += dx;
        viewCoordinates[1] += dy;
    }
}
