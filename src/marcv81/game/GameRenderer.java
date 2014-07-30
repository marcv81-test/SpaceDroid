package marcv81.game;

import marcv81.gfx2d.Renderer;
import marcv81.gfx2d.Sprite;
import android.content.Context;
import java.util.Random;

class GameRenderer extends Renderer {

	private static final int MAX_TIME_BETWEEN_FIREBALLS = 250;
	private static final float CAMERA_SPEED = 2f;

	private static final int SPRITE_BACKGROUND = 0;
	private static final int SPRITE_FIREBALLS = 1;

	private float pointerX = 0f, pointerY = 0f;
	private boolean pointerDown = false;

	private final Sprite[] sprites = { new Background(), new Fireballs() };

	private final Random random = new Random();

	private long gameTime = 0;
	private long nextFireballTime = 0;

	// Constructor
	GameRenderer(Context context) {
		super(context);
	}

	void setPointerDown(boolean pointerDown) {
		this.pointerDown = pointerDown;
	}

	void setPointerXY(float x, float y) {
		this.pointerX = x;
		this.pointerY = y;
	}

	@Override
	public Sprite[] getSprites() {
		return sprites;
	}

	@Override
	protected void update(long timeSlice) {

		// Move camera
		if (pointerDown) {
			float x = -2f * ((pointerX / getWidth()) - 0.5f);
			float y = -2f * ((pointerY / getHeight()) - 0.5f);
			float norm = (float) Math.sqrt(x * x + y * y);
			moveXY((x / norm) * timeSlice / 1000 * CAMERA_SPEED, (y / norm)
					* timeSlice / 1000 * CAMERA_SPEED);
		}

		// Add random fireballs
		gameTime += timeSlice;
		if (gameTime > nextFireballTime) {
			((Fireballs) sprites[SPRITE_FIREBALLS]).add(getCameraX() + 2f
					* (random.nextFloat() - 0.5f), this.getCameraY() + 2f
					* (random.nextFloat() - 0.5f));
			nextFireballTime = gameTime
					+ random.nextInt(MAX_TIME_BETWEEN_FIREBALLS);
		}

		// Update fireballs
		((Fireballs) sprites[SPRITE_FIREBALLS]).update(timeSlice);
	}
}
