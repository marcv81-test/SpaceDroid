package com.test;

import javax.microedition.khronos.opengles.GL10;
import android.content.Context;

public class Background {

	protected static final float BACKGROUND_DEPTH = 8f;
	protected static final float BACKGROUND_SCALE = 8f;

	protected static final int BACKGROUND_RESOURCE = R.drawable.stars;
	protected static final int BACKGROUND_GFX_X = 1;
	protected static final int BACKGROUND_GFX_Y = 1;

	protected final Sprite sprite;

	// Constructor
	public Background() {
		this.sprite = new Sprite(BACKGROUND_SCALE, BACKGROUND_SCALE,
				BACKGROUND_RESOURCE, BACKGROUND_GFX_X, BACKGROUND_GFX_Y);
	}

	// Draw the background
	public void draw(GL10 gl, float x, float y) {

		// Draw the 4 background tiles the closest to the point (x, y)
		x = BACKGROUND_SCALE * (Math.round(x / BACKGROUND_SCALE) - 0.5f);
		y = BACKGROUND_SCALE * (Math.round(y / BACKGROUND_SCALE) - 0.5f);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				sprite.draw(gl, x + BACKGROUND_SCALE * i, y + BACKGROUND_SCALE
						* j, BACKGROUND_DEPTH, 0f, 0);
			}
		}
	}
	
	// Load the sprite texture
	public void loadTexture(GL10 gl, Context context) {
		sprite.loadTexture(gl, context);
	}
}
