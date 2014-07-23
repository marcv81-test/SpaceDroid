package com.test;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;

class TestRenderer extends SpritesRenderer {

	protected final float FOREGROUND_PLANE = 0f;
	protected final float BACKGROUND_PLANE = 8f;

	// Textures
	protected Texture galaxyTexture;
	protected Texture planetTexture;
	protected Texture fireballTexture;

	// Sprites
	protected final Sprite galaxySprite;
	protected final Sprite planetSprite;
	protected final Sprite satelliteSprite;
	protected final Sprite fireballSprite;

	protected long startTime;

	// Constructor
	public TestRenderer(Context context) {

		super(context);

		// Create textures
		this.galaxyTexture = new Texture(1, 1);
		this.planetTexture = new Texture(1, 1);
		this.fireballTexture = new Texture(5, 5);

		// Create sprites
		this.galaxySprite = new Sprite(0f, 0f, BACKGROUND_PLANE, 0f, 10f,
				galaxyTexture);
		this.planetSprite = new Sprite(0f, 0f, FOREGROUND_PLANE, 0f, 0.7f,
				planetTexture);
		this.satelliteSprite = new Sprite(0f, 0f, FOREGROUND_PLANE, 0f, 0.2f,
				planetTexture);
		this.fireballSprite = new Sprite(0f, 0f, FOREGROUND_PLANE, 0f, 0.3f,
				fireballTexture);

		startTime = System.currentTimeMillis();
	}

	@Override
	protected void loadTextures(GL10 gl) {
		galaxyTexture.load(gl, context, R.drawable.galaxy);
		planetTexture.load(gl, context, R.drawable.planet);
		fireballTexture.load(gl, context, R.drawable.fireball);
	}

	@Override
	protected void drawSprites(GL10 gl) {

		float elapsed = (System.currentTimeMillis() - startTime) / 1000f;

		// Adjust sprites angle
		float angle = 57.32f * elapsed;
		planetSprite.setAngle(angle);
		satelliteSprite.setAngle(angle);
		fireballSprite.setAngle(-angle);

		// Adjust sprites position
		satelliteSprite.setXY(0.5f * (float) Math.cos(elapsed),
				0.7f * (float) Math.sin(elapsed));
		fireballSprite.setXY(0.5f * (float) Math.cos(elapsed - 0.2),
				0.7f * (float) Math.sin(elapsed - 0.2));

		// Adjust sprites animation
		fireballSprite.setAnimation((int) (elapsed * 25) % 25);

		// Draw sprites
		galaxySprite.draw(gl);
		planetSprite.draw(gl);
		satelliteSprite.draw(gl);
		fireballSprite.draw(gl);
	}
}
