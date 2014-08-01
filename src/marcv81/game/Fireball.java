package marcv81.game;

import java.util.Random;

class Fireball {

	private static final long FIREBALL_LIFESPAN = 1000; // 1 second
	private static final long FIREBALL_ANIMATIONS = 25;

	private final float x, y;
	private final float startAngle, angleRate;
	private long age = 0;

	// Constructor
	public Fireball(Random random, float x, float y) {

		this.x = x;
		this.y = y;

		// The fireball angles are set randomly
		this.startAngle = 360f * random.nextFloat();
		this.angleRate = 180f * (random.nextFloat() - 0.5f);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	// Return the fireball drawing angle
	public float getAngle() {
		return startAngle + angleRate * age / 1000;
	}

	// Return the fireball animation
	public int getAnimation() {
		long animation = age * FIREBALL_ANIMATIONS / FIREBALL_LIFESPAN;
		return (int) animation;
	}

	// Return whether the fireball has gone past its lifespan or not
	public boolean isExpired() {
		return age >= FIREBALL_LIFESPAN;
	}

	public void update(long timeSlice) {
		age += timeSlice;
	}
}
