package com.test;

import javax.microedition.khronos.opengles.GL10;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;

public class Fireballs {

	protected final float FIREBALL_DEPTH = 0f;
	protected final float FIREBALL_SCALE = 0.3f;
	protected final long FIREBALL_LIFESPAN = 1000; // 1 second
	protected final long FIREBALL_ANIMATIONS = 25;

	protected final Sprite sprite;
	protected final ArrayList<Fireball> fireballs = new ArrayList<Fireball>();

	// Random numbers generator for the fireballs angles
	protected final Random random = new Random();

	// Add a new fireball
	public void add(float x, float y) {
		fireballs.add(new Fireball(x, y));
	}

	// Constructor
	public Fireballs(Texture texture) {
		this.sprite = new Sprite(texture, FIREBALL_SCALE, FIREBALL_SCALE);
	}

	// Draw the fireballs
	public void draw(GL10 gl) {

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
				sprite.draw(gl, fireball.getX(), fireball.getY(),
						FIREBALL_DEPTH, fireball.getAngle(currentTime),
						fireball.getAnimation(currentTime));
			}
		}
	}

	protected class Fireball {

		protected final float x, y, startAngle, angleRate;
		protected final long startTime;

		// Get the fireball x coordinate
		public float getX() {
			return x;
		}

		// Get the fireball y coordinate
		public float getY() {
			return y;
		}

		// Return whether the fireball has gone past its lifespan or not
		public boolean isExpired(long currentTime) {
			return (currentTime - startTime) >= FIREBALL_LIFESPAN;
		}

		// Return the fireball drawing angle
		public float getAngle(long currentTime) {
			return startAngle + angleRate * (currentTime - startTime) / 1000;
		}

		// Return the fireball animation
		public int getAnimation(long currentTime) {
			long animation = (currentTime - startTime) * FIREBALL_ANIMATIONS
					/ FIREBALL_LIFESPAN;
			return (int) animation;
		}

		// Constructor
		// The fireball angles are set randomly
		public Fireball(float x, float y) {
			this.x = x;
			this.y = y;
			this.startAngle = 360f * random.nextFloat();
			this.angleRate = 180f * (random.nextFloat() - 0.5f);
			this.startTime = System.currentTimeMillis();
		}
	}

}
