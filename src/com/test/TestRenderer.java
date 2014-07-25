package com.test;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import java.util.Random;

class TestRenderer extends SpritesRenderer {

	protected final Random random = new Random();

	protected final int MAX_TIME_BETWEEN_FIREBALLS = 250;

	protected long nextFireball = 0;

	// Textures
	protected final Texture backgroundTexture;
	protected final Texture fireballTexture;

	// Scene objects
	protected final Background background;
	protected final Fireballs fireballs;

	// Constructor
	public TestRenderer(Context context) {

		super(context);

		backgroundTexture = new Texture(1, 1);
		this.background = new Background(backgroundTexture);

		fireballTexture = new Texture(5, 5);
		this.fireballs = new Fireballs(fireballTexture);
	}

	@Override
	protected void loadTextures(GL10 gl) {
		backgroundTexture.load(gl, context, R.drawable.stars);
		fireballTexture.load(gl, context, R.drawable.fireball);
	}

	@Override
	protected void drawSprites(GL10 gl) {

		// Add random fireballs
		long currentTime = System.currentTimeMillis();
		if (currentTime > nextFireball) {
			fireballs.add(this.x + 2f * (random.nextFloat() - 0.5f), this.y
					+ 2f * (random.nextFloat() - 0.5f));
			nextFireball = currentTime
					+ random.nextInt(MAX_TIME_BETWEEN_FIREBALLS);
		}

		// Draw the scene objects
		background.draw(gl, this.x, this.y);
		fireballs.draw(gl);
	}
}
