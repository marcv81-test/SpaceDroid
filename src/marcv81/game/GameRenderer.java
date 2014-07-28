package marcv81.game;

import marcv81.gfx2d.Renderer;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import java.util.Random;

class GameRenderer extends Renderer {

	private static final int MAX_TIME_BETWEEN_FIREBALLS = 250;

	private final Random random = new Random();

	private long gameTime = 0;
	private long nextFireballTime = 0;

	// Scene sprites
	private final Background background;
	private final Fireballs fireballs;

	// Constructor
	GameRenderer(Context context) {
		background = new Background(context);
		fireballs = new Fireballs(context);
	}

	@Override
	protected void loadTextures(GL10 gl) {
		background.loadTexture(gl);
		fireballs.loadTexture(gl);
	}

	@Override
	protected void update(long timeSlice) {

		// Add random fireballs
		gameTime += timeSlice;
		if (gameTime > nextFireballTime) {
			fireballs.add(this.x + 2f * (random.nextFloat() - 0.5f), this.y
					+ 2f * (random.nextFloat() - 0.5f));
			nextFireballTime = gameTime
					+ random.nextInt(MAX_TIME_BETWEEN_FIREBALLS);
		}

		// Update fireballs
		fireballs.update(timeSlice);
	}

	@Override
	protected void drawSprites(GL10 gl) {

		// Draw the scene objects
		background.draw(gl, this.x, this.y);
		fireballs.draw(gl);
	}
}
