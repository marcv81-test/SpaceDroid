package marcv81.game;

import marcv81.gfx2d.Renderer;
import marcv81.gfx2d.Sprite;
import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

class GameRenderer extends Renderer {

	private static final float CAMERA_SPEED = 2f;

	private static final float FOREGROUND_DEPTH = 0f;
	private static final float BACKGROUND_DEPTH = 8f;

	private static final int SPRITE_BACKGROUND = 0;
	private static final int SPRITE_FIREBALL = 1;
	private static final int SPRITE_ASTEROID = 2;

	private static final int ASTEROID_MAX_COUNT = 25;

	// Touchscreen status
	private float pointerX = 0f, pointerY = 0f;
	private boolean pointerDown = false;

	private final Random random = new Random();

	private final Sprite[] sprites = { new BackgroundSprite(),
			new FireballSprite(), new AsteroidSprite() };

	private final ArrayList<Fireball> fireballs = new ArrayList<Fireball>();
	private final ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();

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
	protected Sprite[] getSprites() {
		return sprites;
	}

	@Override
	protected void update(long timeSlice) {
		updateCamera(timeSlice);
		updateAsteroids(timeSlice);
		updateFireballs(timeSlice);
	}

	@Override
	protected void draw(GL10 gl) {
		drawBackground(gl);
		drawAsteroids(gl);
		drawFireballs(gl);
	}

	private void updateCamera(long timeSlice) {
		if (pointerDown) {
			float x = -2f * ((pointerX / getWidth()) - 0.5f);
			float y = -2f * ((pointerY / getHeight()) - 0.5f);
			float norm = (float) Math.sqrt(x * x + y * y);
			float dx = (x / norm) * timeSlice / 1000 * CAMERA_SPEED;
			float dy = (y / norm) * timeSlice / 1000 * CAMERA_SPEED;
			moveXY(dx, dy);
		}
	}

	private void updateAsteroids(long timeSlice) {

		Iterator<Asteroid> asteroidIterator = asteroids.iterator();
		while (asteroidIterator.hasNext()) {

			Asteroid asteroid = asteroidIterator.next();
			asteroid.update(timeSlice);

			// Remove the asteroids which are too far
			if (asteroid.isExpired(getCameraX(), getCameraY())) {
				asteroidIterator.remove();
			}
		}

		// Add asteroids if we have space
		while (asteroids.size() < ASTEROID_MAX_COUNT) {
			asteroids.add(new Asteroid(random, getCameraX(), getCameraY()));
		}
	}

	private void updateFireballs(long timeSlice) {

		Iterator<Fireball> fireballIterator = fireballs.iterator();
		while (fireballIterator.hasNext()) {

			Fireball fireball = fireballIterator.next();
			fireball.update(timeSlice);

			// Remove the expired fireballs
			if (fireball.isExpired()) {
				fireballIterator.remove();
			}
		}
	}

	// Draw the 4 background tiles the closest to the camera
	private void drawBackground(GL10 gl) {
		float x1 = Math.round(getCameraX() / BackgroundSprite.BACKGROUND_SCALE);
		float y1 = Math.round(getCameraY() / BackgroundSprite.BACKGROUND_SCALE);
		for (float x2 : new float[] { x1 - 0.5f, x1 + 0.5f }) {
			for (float y2 : new float[] { y1 - 0.5f, y1 + 0.5f }) {
				sprites[SPRITE_BACKGROUND].draw(gl,
						BackgroundSprite.BACKGROUND_SCALE * x2,
						BackgroundSprite.BACKGROUND_SCALE * y2,
						BACKGROUND_DEPTH, 0f, 0);
			}
		}
	}

	private void drawAsteroids(GL10 gl) {
		for (Asteroid asteroid : asteroids) {
			sprites[SPRITE_ASTEROID].draw(gl, asteroid.getX(), asteroid.getY(),
					FOREGROUND_DEPTH, 0f, asteroid.getAnimation());
		}
	}

	private void drawFireballs(GL10 gl) {
		for (Fireball fireball : fireballs) {
			sprites[SPRITE_FIREBALL].draw(gl, fireball.getX(), fireball.getY(),
					FOREGROUND_DEPTH, fireball.getAngle(),
					fireball.getAnimation());
		}
	}
}
