package marcv81.game;

import marcv81.gfx2d.Sprite;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;

class Fireballs extends Sprite {

	private static final float FIREBALL_DEPTH = 0f;
	private static final float FIREBALL_SCALE = 0.3f;

	private static final int FIREBALL_RESOURCE = R.drawable.fireball;
	private static final int FIREBALL_GFX_X = 5;
	private static final int FIREBALL_GFX_Y = 5;

	private static final long FIREBALL_LIFESPAN = 1000; // 1 second
	private static final long FIREBALL_ANIMATIONS = 25;

	private final ArrayList<Fireball> fireballs = new ArrayList<Fireball>();

	// Random numbers generator for the fireballs angles
	private final Random random = new Random();

	// Add a new fireball
	void add(float x, float y) {
		fireballs.add(new Fireball(x, y));
	}

	// Constructor
	Fireballs(Context context) {
		super(context, FIREBALL_RESOURCE, FIREBALL_GFX_X, FIREBALL_GFX_Y,
				FIREBALL_SCALE, FIREBALL_SCALE);
	}

	// Draw the fireballs
	void draw(GL10 gl) {

		long currentTime = System.currentTimeMillis();

		// Iterate through the fireballs
		Iterator<Fireball> iterator = fireballs.iterator();
		while (iterator.hasNext()) {

			Fireball fireball = iterator.next();

			// Remove the expired fireballs
			if (fireball.isExpired(currentTime)) {
				iterator.remove();
			}

			// Draw the remaining fireballs
			else {
				draw(gl, fireball.x, fireball.y, FIREBALL_DEPTH,
						fireball.getAngle(currentTime),
						fireball.getAnimation(currentTime));
			}
		}
	}

	private class Fireball {

		private final float x, y, startAngle, angleRate;
		private final long startTime;

		// Return whether the fireball has gone past its lifespan or not
		private boolean isExpired(long currentTime) {
			return (currentTime - startTime) >= FIREBALL_LIFESPAN;
		}

		// Return the fireball drawing angle
		private float getAngle(long currentTime) {
			return startAngle + angleRate * (currentTime - startTime) / 1000;
		}

		// Return the fireball animation
		private int getAnimation(long currentTime) {
			long animation = (currentTime - startTime) * FIREBALL_ANIMATIONS
					/ FIREBALL_LIFESPAN;
			return (int) animation;
		}

		// Constructor
		// The fireball angles are set randomly
		private Fireball(float x, float y) {
			this.x = x;
			this.y = y;
			this.startAngle = 360f * random.nextFloat();
			this.angleRate = 180f * (random.nextFloat() - 0.5f);
			this.startTime = System.currentTimeMillis();
		}
	}
}
