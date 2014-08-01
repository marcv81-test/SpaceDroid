package marcv81.game;

import marcv81.gfx2d.Sprite;

class FireballSprite extends Sprite {

	private static final float FIREBALL_SCALE = 0.4f;

	private static final int FIREBALL_RESOURCE = R.drawable.fireball;
	private static final int FIREBALL_GFX_X = 5;
	private static final int FIREBALL_GFX_Y = 5;

	// Constructor
	FireballSprite() {
		super(FIREBALL_RESOURCE, FIREBALL_GFX_X, FIREBALL_GFX_Y, FIREBALL_SCALE);
	}
}