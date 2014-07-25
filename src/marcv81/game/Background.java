package marcv81.game;

import marcv81.gfx2d.Sprite;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;

public class Background extends Sprite {

	protected static final float BACKGROUND_DEPTH = 8f;
	protected static final float BACKGROUND_SCALE = 8f;

	protected static final int BACKGROUND_RESOURCE = R.drawable.stars;
	protected static final int BACKGROUND_GFX_X = 1;
	protected static final int BACKGROUND_GFX_Y = 1;

	// Constructor
	public Background(Context context) {
		super(context, BACKGROUND_RESOURCE, BACKGROUND_GFX_X, BACKGROUND_GFX_Y,
				BACKGROUND_SCALE, BACKGROUND_SCALE);
	}

	// Draw the background
	public void draw(GL10 gl, float x, float y) {

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
