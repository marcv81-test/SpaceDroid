package marcv81.game;

import marcv81.gfx2d.Renderer;
import marcv81.gfx2d.Sprite;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import java.util.Random;

class GameRenderer extends Renderer {

	private static final int MAX_TIME_BETWEEN_FIREBALLS = 250;

	private static final int SPRITE_BACKGROUND = 0;
	private static final int SPRITE_FIREBALLS = 1;

	private final Sprite[] sprites = { new Background(), new Fireballs() };

	private final Random random = new Random();

	private long gameTime = 0;
	private long nextFireballTime = 0;

	// Constructor
	GameRenderer(Context context) {
		super(context);
	}

	@Override
	public Sprite[] getSprites() {
		return sprites;
	}

	@Override
	protected void update(long timeSlice) {

		// Add random fireballs
		gameTime += timeSlice;
		if (gameTime > nextFireballTime) {
			((Fireballs) sprites[SPRITE_FIREBALLS]).add(
					this.x + 2f * (random.nextFloat() - 0.5f), this.y + 2f
							* (random.nextFloat() - 0.5f));
			nextFireballTime = gameTime
					+ random.nextInt(MAX_TIME_BETWEEN_FIREBALLS);
		}

		// Update fireballs
		((Fireballs) sprites[SPRITE_FIREBALLS]).update(timeSlice);
	}
}
