package marcv81.game;

import marcv81.gfx2d.Sprite;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;

class Background extends Sprite {

	private static final float BACKGROUND_DEPTH = 8f;
	private static final float BACKGROUND_SCALE = 8f;

	private static final int BACKGROUND_RESOURCE = R.drawable.stars;
	private static final int BACKGROUND_GFX_X = 1;
	private static final int BACKGROUND_GFX_Y = 1;

	// Constructor
	Background(Context context) {
		super(context, BACKGROUND_RESOURCE, BACKGROUND_GFX_X, BACKGROUND_GFX_Y,
				BACKGROUND_SCALE, BACKGROUND_SCALE);
	}

	// Draw the background
	void draw(GL10 gl, float x, float y) {

		// Draw the 4 background tiles the closest to the point (x, y)
		x = BACKGROUND_SCALE * (Math.round(x / BACKGROUND_SCALE) - 0.5f);
		y = BACKGROUND_SCALE * (Math.round(y / BACKGROUND_SCALE) - 0.5f);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				draw(gl, x + BACKGROUND_SCALE * i, y + BACKGROUND_SCALE * j,
						BACKGROUND_DEPTH, 0f, 0);
			}
		}
	}
}
