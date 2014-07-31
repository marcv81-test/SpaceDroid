package marcv81.game;

import marcv81.gfx2d.Sprite;
import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Asteroids extends Sprite {

	private final static float TAU = 6.2831853071f;

	private static final float ASTEROID_DEPTH = 0f;
	private static final float ASTEROID_SCALE = 0.4f;

	private static final int ASTEROID_RESOURCE = R.drawable.asteroid;
	private static final int ASTEROID_GFX_X = 8;
	private static final int ASTEROID_GFX_Y = 8;

	private static final int ASTEROID_ANIMATIONS = 32;
	private static final int ASTEROID_ANIMATIONS_TYPES = 2;

	private final static float ASTEROID_SPAWN_DISTANCE = 4f;
	private final static float ASTEROID_REMOVAL_DISTANCE = 6f;
	private final static float ASTEROID_DRIFT_MIN_SPEED = 0.1f;
	private final static float ASTEROID_DRIFT_MAX_SPEED = 0.5f;
	private final static int ASTEROID_ANIMATION_MIN_SPEED = 25;
	private final static int ASTEROID_ANIMATION_MAX_SPEED = 30;

	private final static int ASTEROID_MAX_COUNT = 25;

	private final ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();

	private final Random random = new Random();

	// Constructor
	Asteroids() {
		super(ASTEROID_RESOURCE, ASTEROID_GFX_X, ASTEROID_GFX_Y,
				ASTEROID_SCALE, ASTEROID_SCALE);
	}

	public void update(long timeSlice, float cameraX, float cameraY) {

		// Iterate through the asteroids
		Iterator<Asteroid> iterator = asteroids.iterator();
		while (iterator.hasNext()) {

			Asteroid asteroid = iterator.next();
			asteroid.update(timeSlice);

			// Remove the asteroids which are too far
			if (asteroid.distance(cameraX, cameraY) > ASTEROID_REMOVAL_DISTANCE) {
				iterator.remove();
			}
		}

		// Add asteroids if we have space
		while (asteroids.size() < ASTEROID_MAX_COUNT) {
			asteroids.add(new Asteroid(cameraX, cameraY));
		}
	}

	// Draw the asteroids
	public void drawAll(GL10 gl, float x, float y) {
		for (Asteroid asteroid : asteroids) {
			drawOne(gl, asteroid.x, asteroid.y, ASTEROID_DEPTH, 0f,
					asteroid.getAnimation());
		}
	}

	private class Asteroid {

		private float x, y;
		private final float speedX, speedY;
		private final int animationType;
		private final int animationSpeed;
		private final int animationDirection;
		private long age = 0;

		// Constructor
		// Initial position is at a fixed distance from the camera
		// Speed is such that the asteroid vaguely heads towards the camera
		private Asteroid(float x, float y) {

			// Start position
			float angle = 6.2831853071f * random.nextFloat();
			this.x = x + ASTEROID_SPAWN_DISTANCE * (float) Math.cos(angle);
			this.y = y + ASTEROID_SPAWN_DISTANCE * (float) Math.sin(angle);

			// Drift speed
			float driftAngle = angle + (TAU / 2f)
					+ (2f * (random.nextFloat() - 0.5f) * (TAU / 4f));
			float driftSpeed = ASTEROID_DRIFT_MIN_SPEED + random.nextFloat()
					* (ASTEROID_DRIFT_MAX_SPEED - ASTEROID_DRIFT_MIN_SPEED);
			this.speedX = driftSpeed * (float) Math.cos(driftAngle);
			this.speedY = driftSpeed * (float) Math.sin(driftAngle);

			// Animation properties
			this.animationType = random.nextInt(ASTEROID_ANIMATIONS_TYPES);
			this.animationSpeed = ASTEROID_ANIMATION_MIN_SPEED
					+ random.nextInt(ASTEROID_ANIMATION_MAX_SPEED
							- ASTEROID_ANIMATION_MIN_SPEED + 1);
			this.animationDirection = random.nextInt(2);
		}

		// Update the asteroid
		private void update(long timeSlice) {
			x += speedX * timeSlice / 1000f;
			y += speedY * timeSlice / 1000f;
			age += timeSlice;
		}

		// Return the asteroid animation
		private int getAnimation() {
			int animation = (int) (animationSpeed * age / 1000 % ASTEROID_ANIMATIONS);
			animation = (animationDirection == 0) ? animation
					: ASTEROID_ANIMATIONS - animation - 1;
			return animation + (ASTEROID_ANIMATIONS * animationType);
		}

		// Return the distance between the asteroid and the camera
		private float distance(float cameraX, float cameraY) {
			return (float) Math.sqrt((cameraX - this.x) * (cameraX - this.x)
					+ (cameraY - this.y) * (cameraY - this.y));
		}
	}
}
