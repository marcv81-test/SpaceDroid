package marcv81.game;

import marcv81.gfx2d.Sprite;

public class AsteroidSprite extends Sprite {

	private static final float ASTEROID_SCALE = 0.4f;

	private static final int ASTEROID_RESOURCE = R.drawable.asteroid;
	private static final int ASTEROID_GFX_X = 8;
	private static final int ASTEROID_GFX_Y = 8;

	// Constructor
	AsteroidSprite() {
		super(ASTEROID_RESOURCE, ASTEROID_GFX_X, ASTEROID_GFX_Y, ASTEROID_SCALE);
	}
}
