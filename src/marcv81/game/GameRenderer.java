package marcv81.game;

import marcv81.gfx2d.Renderer;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import java.util.Random;

class GameRenderer extends Renderer {

	protected static final int MAX_TIME_BETWEEN_FIREBALLS = 250;

	protected final Random random = new Random();
	protected long nextFireball = 0;

	// Scene objects
	protected final Background background;
	protected final Fireballs fireballs;

	// Constructor
	public GameRenderer(Context context) {
		background = new Background(context);
		fireballs = new Fireballs(context);
	}

	@Override
	protected void loadTextures(GL10 gl) {
		background.loadTexture(gl);
		fireballs.loadTexture(gl);
	}

	@Override
	protected void drawSprites(GL10 gl) {

		// Add random fireballs
		long currentTime = System.currentTimeMillis();
		if (currentTime > nextFireball) {
			fireballs.add(this.x + 2f * (random.nextFloat() - 0.5f), this.y
					+ 2f * (random.nextFloat() - 0.5f));
			nextFireball = currentTime
					+ random.nextInt(MAX_TIME_BETWEEN_FIREBALLS);
		}

		// Draw the scene objects
		background.draw(gl, this.x, this.y);
		fireballs.draw(gl);
	}
}
