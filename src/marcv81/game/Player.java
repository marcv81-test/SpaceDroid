package marcv81.game;

import marcv81.gfx2d.Gfx2DSprite;
import javax.microedition.khronos.opengles.GL10;

class Player extends Gfx2DSprite {

	private static final float PLAYER_DEPTH = 0f;
	private static final float PLAYER_SCALE = 0.2f;

	private static final int PLAYER_RESOURCE = R.drawable.player;
	private static final int PLAYER_GFX_X = 1;
	private static final int PLAYER_GFX_Y = 1;

	private float angle = 0;

	// Constructor
	Player() {
		super(PLAYER_RESOURCE, PLAYER_GFX_X, PLAYER_GFX_Y,
				PLAYER_SCALE, PLAYER_SCALE);
	}

	void setAngle(float angle) {
		this.angle = angle;
	}

	@Override
	public void drawAll(GL10 gl, float x, float y) {
		drawOne(gl, x, y, PLAYER_DEPTH, angle, 0);
	}
}
