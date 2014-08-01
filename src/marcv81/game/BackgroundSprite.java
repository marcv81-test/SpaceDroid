package marcv81.game;

import marcv81.gfx2d.Sprite;

class BackgroundSprite extends Sprite {

	public static final float BACKGROUND_SCALE = 8f;

	private static final int BACKGROUND_RESOURCE = R.drawable.stars;
	private static final int BACKGROUND_GFX_X = 1;
	private static final int BACKGROUND_GFX_Y = 1;

	// Constructor
	BackgroundSprite() {
		super(BACKGROUND_RESOURCE, BACKGROUND_GFX_X, BACKGROUND_GFX_Y,
				BACKGROUND_SCALE);
	}
}
