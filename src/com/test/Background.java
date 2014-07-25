package com.test;

import javax.microedition.khronos.opengles.GL10;

public class Background {

	protected final float BACKGROUND_DEPTH = 8f;
	protected final float BACKGROUND_SCALE = 8f;

	protected final Sprite sprite;

	// Constructor
	public Background(Texture texture) {
		this.sprite = new Sprite(texture, BACKGROUND_SCALE, BACKGROUND_SCALE);
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
}
